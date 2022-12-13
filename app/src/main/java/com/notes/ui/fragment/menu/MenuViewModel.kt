package com.notes.ui.fragment.menu

import com.notes.auth.AuthManager
import com.notes.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Fedotov Yakov
 */
@HiltViewModel
class MenuViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val interactor: MenuInteractor
) : BaseViewModel() {

    override fun onStart() {
        /* no-op */
    }

    fun logOut() {
        interactor.clearDB().handleResult()
        authManager.logOut()
    }
}