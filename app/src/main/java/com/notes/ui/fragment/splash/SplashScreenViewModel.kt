package com.notes.ui.fragment.splash

import com.notes.auth.AuthManager
import com.notes.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Fedotov Yakov
 */
@HiltViewModel
class SplashScreenViewModel @Inject constructor(private val authManager: AuthManager) :
    BaseViewModel() {


    override fun onStart() {
        if (authManager.currentUser == null) {
/*            authManager.signInAnonymously {
                it
            }*/
        }
    }
}