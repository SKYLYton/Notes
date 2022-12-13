package com.notes.ui.fragment.auth

import com.notes.auth.AuthManager
import com.notes.db.firestore.FirestoreManager
import com.notes.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

/**
 * @author Fedotov Yakov
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val firestoreManager: FirestoreManager
) : BaseViewModel() {
    private val _authState = MutableSharedFlow<Boolean>()
    val authState = _authState.asSharedFlow()


    override fun onStart() {
        /* no-op */
    }

    fun auth(email: String, password: String) {
        authManager.loginEmailPassword(email, password, {
            firestoreManager.getNotes()
            emit(_authState, true)
        }) {
            emit(_error, it)
        }
    }
}