package com.notes.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.notes.R
import com.notes.databinding.ActivityMainBinding
import com.notes.extension.setupWithNavController
import com.notes.ui.base.BaseActivity
import com.notes.ui.fragment.splash.SplashScreenFragmentDirections
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private var currentNavController: LiveData<NavController>? = null

    var navController: NavController? = null
        private set


    private val onDestinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->

            // if you need to show/hide bottom nav or toolbar based on destination
            // binding.bottomNavigationView.isVisible = destination.id != R.id.itemDetail
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.apply {
            start()
        }

        actionBar?.hide()
        supportActionBar?.hide()

        if (savedInstanceState == null) {
            setUpBottomNavigationBar()
        }
        processSplash()
    }

    private fun processSplash() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if (viewModel.isAuth()) {
                        navController?.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToNotesFragment())
                    } else {
                        navController?.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToAuthFragment())
                    }
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    return true
                }
            }
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setUpBottomNavigationBar()
    }

    private fun setUpBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.main_navigation,
            R.navigation.menu_navigation
        )

        val controller = binding.navView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )

        controller.observe(this) { navController ->
            this.navController = navController
            setupActionBarWithNavController(navController)

            // unregister old onDestinationChangedListener, if it exists
            currentNavController?.value?.removeOnDestinationChangedListener(
                onDestinationChangedListener
            )

            // add onDestinationChangedListener to the new NavController
            navController.addOnDestinationChangedListener(onDestinationChangedListener)
        }

        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    fun bottomNavVisible(isVisible: Boolean) {
        if (::binding.isInitialized) {
            binding.navView.isVisible = isVisible
        }
    }

    val isLandscape: Boolean
        get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

}