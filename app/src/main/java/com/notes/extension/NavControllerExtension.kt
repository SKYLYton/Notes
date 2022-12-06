package com.notes.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController

/**
 * @author Fedotov Yakov
 */
fun <T> NavController.subscribeOnResult(
    key: String,
    owner: LifecycleOwner,
    clearAfterCall: Boolean = false,
    observer: Observer<T?>
) {
    removeSubscribe<T?>(key)
    val liveData = currentBackStackEntry?.savedStateHandle?.getLiveData<T?>(key)
    liveData?.observe(owner) { t ->
        observer.onChanged(t)
        if (clearAfterCall) {
            removeSubscribe<T?>(key)
        }
    }
}

fun <T> NavController.removeSubscribe(key: String) {
    currentBackStackEntry?.savedStateHandle?.remove<T>(key)
}

fun <T> NavController.sendResult(key: String, obj: T?, popBackStack: Boolean = false) {
    previousBackStackEntry?.savedStateHandle?.set(key, obj)
    if (popBackStack) {
        popBackStack()
    }
}