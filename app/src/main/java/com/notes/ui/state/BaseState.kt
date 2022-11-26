package com.notes.ui.state

/**
 * @author Fedotov Yakov
 */
sealed class BaseState<out T> {
    data class Loading(val isLoading: Boolean) : BaseState<Nothing>()
    data class Error(val throwable: Throwable) : BaseState<Nothing>()
    data class Success<T>(val item: T) : BaseState<T>()
}