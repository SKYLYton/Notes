package com.notes.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.notes.R
import com.notes.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint




@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActivityRouter {

    private lateinit var binding: ActivityMainBinding

    private lateinit var leftController: NavController
    private lateinit var rightController: NavController
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar?.hide()
        supportActionBar?.hide()

        if (isLandscape) {
            setLandscapeNavigation()
        } else {
            setPortraitNavigation()
        }

    }

    private fun setLandscapeNavigation() {
        initLeftNavHost()
        initRightNavHost()
    }

    private fun initLeftNavHost() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.left_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.left_navigation)
        graph.setStartDestination(R.id.notesFragment)
        navController.graph = graph
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        leftController = navController
    }

    private fun initRightNavHost() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.right_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.right_navigation)
        //graph.setStartDestination(R.id.notesFragment)
        navController.graph = graph
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        rightController = navController
    }

    private fun setPortraitNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.main_navigation)
        graph.setStartDestination(R.id.notesFragment)
        navController.graph = graph
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        this.navController = navController
    }

    override fun navigationTo(screen: Screen, bundle: Bundle?) {
        if (isLandscape) {
            when (screen) {
                Screen.Notes -> leftController.navigate(screen.id, bundle)
                Screen.EditNote -> {
                    rightController.popBackStack()
                    rightController.navigate(screen.id, bundle)
                }
            }
        } else {
            navController.navigate(screen.id, bundle)
        }
    }

    private val isLandscape: Boolean
        get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}