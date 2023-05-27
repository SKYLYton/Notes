package com.notes.ui.base

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.Transition.TransitionListener
import androidx.transition.TransitionManager
import androidx.viewbinding.ViewBinding
import com.notes.R
import com.notes.ui.activity.MainActivity
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

/**
 * @author Fedotov Yakov
 */
private typealias InflateDialog<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

open class BaseDialogFragment<Binding : ViewBinding>(private val inflate: InflateDialog<Binding>) :
    DialogFragment() {

    private var binding: Binding? = null

    /**
     * Устанавливает blur эффект
     */
    protected open val isBlurEffectEnabled: Boolean = true

    /**
     * Является ли статус бар темным
     */
    protected open val isStatusBarLight: Boolean = false

    /**
     * Главный контейнер, к элементам которого будет применяться анимация
     */
    protected var container: ViewGroup? = null

    private var blurView: BlurView? = null

    private val transition = AutoTransition().apply {
        duration = DURATION_ANIM
    }

    protected open var dismissAnim: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        processArguments(requireArguments())
        processListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate.invoke(inflater, container, false)
        return binding!!.root
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnKeyListener { _, keyCode, event ->
            var result = false
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                dismissWithAnim()
                result = true
            }
            result
        }
        if (isBlurEffectEnabled) {
            //Устанавливаем полноэкранный диалог и прозрачный статусбар
            dialog?.window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent)
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
                setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )

                WindowInsetsControllerCompat(
                    this,
                    view
                ).apply {
                    if (isAppearanceLightStatusBars != isStatusBarLight) {
                        isAppearanceLightStatusBars = isStatusBarLight
                    }
                }
            }

            //Устанавливаем блюр
            blurView = blurInjector(view as? ViewGroup)
            blurView?.alpha = 0f
            blurView!!.setupWith(requireActivity().window.decorView as ViewGroup)
                .setFrameClearDrawable(requireActivity().window.decorView.background)
                .setBlurAlgorithm(RenderScriptBlur(requireContext()))
                .setHasFixedTransformationMatrix(!DEFAULT_BLUR_LIVE)
                .setBlurRadius(DEFAULT_BLUR_RADIUS)
                .setOverlayColor(resources.getColor(R.color.background_5, null))

            blurView?.post {
                blurView?.animate()?.apply {
                    duration = 200
                    alpha(1f)
                }?.start()
            }

            if (isCancelable) {
                blurView?.setOnClickListener {
                    dismissWithAnim()
                }
            }
        }
    }

    protected open fun processArguments(bundle: Bundle) {
        /* no-op */
    }

    protected fun onActionPerformed(bundle: Bundle) {
        setFragmentResult(ACTION, bundle)
    }

    private fun onDismissed() {
        setFragmentResult(DISMISS, Bundle.EMPTY)
    }

    private fun processListener() {
        setFragmentResultListener(ACTION, listener::onActionPerformed)
        setFragmentResultListener(DISMISS, listener::onActionPerformed)
    }

    protected fun dismissWithAnim() {
        if (dismissAnim != null) {
            dismissAnim?.invoke()
            dismissAnim = null
            return
        }
        blurView?.animate()?.apply {
            duration = 200
            alpha(0f)
            setListener(object : AnimatorListener {
                override fun onAnimationStart(p0: Animator) {

                }

                override fun onAnimationEnd(p0: Animator) {
                    dismiss()
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }

            })
        }?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun setCancelable(cancelable: Boolean) {
        super.setCancelable(cancelable)
        if (cancelable) {
            blurView?.setOnClickListener {
                dismiss()
            }
        } else {
            blurView?.setOnClickListener(null)
        }
    }

    protected fun runBinding(block: Binding.() -> Unit) {
        binding?.let(block)
    }

    protected fun runBindingWithAnim(onEnd: (() -> Unit)? = null, block: Binding.() -> Unit) {
        runWithAnim(onEnd)
        binding?.let(block)
    }

    protected fun runWithAnim(onEnd: (() -> Unit)? = null) {
        container?.let {
            transition.addListener(object : TransitionListener {
                override fun onTransitionStart(transition: Transition) {
                    /* no-op */
                }

                override fun onTransitionEnd(transition: Transition) {
                    onEnd?.invoke()
                }

                override fun onTransitionCancel(transition: Transition) {
                    /* no-op */
                }

                override fun onTransitionPause(transition: Transition) {
                    /* no-op */
                }

                override fun onTransitionResume(transition: Transition) {
                    /* no-op */
                }

            })
            TransitionManager.beginDelayedTransition(it, transition)
        }
    }

    /**
     * Добавляет blurview
     */
    private fun blurInjector(container: ViewGroup?) =
        BlurView(requireContext()).apply {
            id = BLUR_ID
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            container?.addView(this, 0, layoutParams)
        }

    protected fun addHandleBackCallBack(callback: OnBackPressedCallback) {
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    protected fun onBackPressed() {
        mainActivity?.onBackPressedDispatcher?.onBackPressed()
    }

    protected val isLandscape: Boolean
        get() = (activity as MainActivity).isLandscape

    protected val mainActivity: MainActivity?
        get() = activity as? MainActivity

    private val listener: DialogActionListener
        get() = parentFragment as DialogActionListener

}

interface DialogActionListener {
    fun onActionPerformed(code: String, result: Bundle)
}

private const val BLUR_ID = 514214

private const val DEFAULT_BLUR_RADIUS = 3f
private const val DEFAULT_BLUR_LIVE = false

const val ACTION = "ACTION"
const val DISMISS = "DISMISS"

const val DURATION_ANIM = 250L