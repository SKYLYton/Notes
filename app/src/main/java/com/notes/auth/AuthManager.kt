package com.notes.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser

/**
 * @author Fedotov Yakov
 */
class AuthManager(
    private val auth: FirebaseAuth
) {

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val isAuth: Boolean
        get() = currentUser != null

    fun logOut() {
        auth.signOut()
    }

    fun registerEmailPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (msg: String) -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                currentUser?.sendEmailVerification()
                processResult(task.isSuccessful, onSuccess, onError, task.exception)
            }
    }

    fun loginEmailPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (msg: String) -> Unit,
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                processResult(task.isSuccessful, onSuccess, onError, task.exception)
            }
    }

    private fun processResult(
        isSuccess: Boolean,
        onSuccess: () -> Unit,
        onError: (msg: String) -> Unit,
        throwable: Throwable? = null
    ) {
        if (isSuccess) {
            onSuccess()
        } else {
            val msg = FirebaseAuthError.fetchMsgByCode(
                (throwable as? FirebaseAuthException)?.errorCode
            )
            onError(msg)
        }
    }

}