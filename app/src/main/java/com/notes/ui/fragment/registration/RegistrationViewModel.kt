package com.notes.ui.fragment.registration

import com.notes.auth.AuthManager
import com.notes.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

/**
 * @author Fedotov Yakov
 */
@HiltViewModel
class RegistrationViewModel @Inject constructor(private val authManager: AuthManager) :
    BaseViewModel() {
    private val _registerState = MutableSharedFlow<Boolean>()
    val registerState = _registerState.asSharedFlow()

    override fun onStart() {
        /* no-op */
    }

    fun register(email: String, password: String) {
        authManager.registerEmailPassword(email, password, {
            emit(_registerState, true)
        }) {
            emit(_error, it)
        }
    }
}