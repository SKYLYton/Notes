package com.notes.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.google.android.material.appbar.AppBarLayout
import com.notes.R
import com.notes.databinding.ViewDoubleAppBarBinding

/**
 * @author Fedotov Yakov
 */
class DoubleAppBar(
    context: Context,
    attrs: AttributeSet?
) : AppBarLayout(context, attrs) {

    private val binding = ViewDoubleAppBarBinding.inflate(LayoutInflater.from(context), this)

    private val transition = Fade()

    private var defaultTitle: String

    var navigationOnClickListener: ((TypeTopAppBar) -> Unit)? = null
    var setAppBarOnMenuItemClickListener: ((MenuItem) -> Unit)? = null
    var setSecondAppBarOnMenuItemClickListener: ((MenuItem) -> Boolean)? = null

    val firstMenu: Menu
        get() = binding.topAppBar.menu

    init {

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DoubleAppBar,
            0, 0
        ).apply {
            val firstMenu =
                getResourceId(R.styleable.DoubleAppBar_firstMenu, 0)

            val secondMenu =
                getResourceId(R.styleable.DoubleAppBar_secondMenu, 0)

            val navigationIcon =
                getDrawable(R.styleable.DoubleAppBar_navigationIcon)

            defaultTitle =
                getString(R.styleable.DoubleAppBar_title) ?: ""

            binding.secondTopAppBar.title = defaultTitle
            binding.topAppBar.title = defaultTitle


            if (firstMenu != 0) {
                setMenu(TypeTopAppBar.FIRST, firstMenu)
            }

            if (secondMenu != 0) {
                setMenu(TypeTopAppBar.SECOND, secondMenu)
            }

            navigationIcon?.let {
                binding.topAppBar.navigationIcon = it
            }
        }

        binding.topAppBar.setOnMenuItemClickListener {
            setAppBarOnMenuItemClickListener?.invoke(it)
            true
        }

        binding.secondTopAppBar.setOnMenuItemClickListener {
            val hideSecondBar = setSecondAppBarOnMenuItemClickListener?.invoke(it)
            visibleSecondMenu(!(hideSecondBar ?: false))
            true
        }

        binding.topAppBar.setNavigationOnClickListener {
            navigationOnClickListener?.invoke(TypeTopAppBar.FIRST)
        }

        binding.secondTopAppBar.setNavigationOnClickListener {
            visibleSecondMenu(false)
            navigationOnClickListener?.invoke(TypeTopAppBar.SECOND)
        }
    }

    fun setMenu(typeTopAppBar: TypeTopAppBar, @MenuRes menuRes: Int) {
        when (typeTopAppBar) {
            TypeTopAppBar.FIRST -> binding.topAppBar.inflateMenu(menuRes)
            TypeTopAppBar.SECOND -> binding.secondTopAppBar.inflateMenu(menuRes)
        }
    }

    fun visibleSecondMenu(visible: Boolean) {
        binding.apply {
            if (topAppBar.isVisible != !visible && secondTopAppBar.isVisible != visible) {
                TransitionManager.beginDelayedTransition(this@DoubleAppBar, transition)
                topAppBar.isVisible = !visible
                secondTopAppBar.isVisible = visible
            }
        }
    }

    fun setTitle(@StringRes stringRes: Int) {
        defaultTitle = resources.getString(stringRes)
        binding.topAppBar.setTitle(stringRes)
        binding.secondTopAppBar.setTitle(stringRes)
    }

    fun setSecondTitle(text: String) {
        binding.secondTopAppBar.title = text
    }
}

enum class TypeTopAppBar {
    FIRST, SECOND
}