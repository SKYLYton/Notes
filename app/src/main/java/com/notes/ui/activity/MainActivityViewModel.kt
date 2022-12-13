package com.notes.ui.activity

import com.notes.auth.AuthManager
import com.notes.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Fedotov Yakov
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authManager: AuthManager
) : BaseViewModel() {

    override fun onStart() {
        /* no-op */
    }

    fun isAuth(): Boolean {
        return authManager.isAuth
    }
}