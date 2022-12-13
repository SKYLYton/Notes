package com.notes.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.notes.ui.state.BaseState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author Fedotov Yakov
 */
abstract class BaseViewModel : ViewModel() {

    private val _navigation = MutableSharedFlow<NavDirections>()
    val navigation = _navigation.asSharedFlow()

    protected val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    private var isStarted: Boolean = false

    lateinit var navController: NavController

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        _error.tryEmit(throwable.message ?: "")
    }

    protected abstract fun onStart()

    fun start() {
        if (!isStarted) {
            isStarted = true
            onStart()
        }
    }

    fun start(navController: NavController) {
        this.navController = navController
        if (!isStarted) {
            isStarted = true
            onStart()
        }
    }

    protected fun <T> Flow<T>.handleResult(
        onLoading: ((Boolean) -> Unit)? = null,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) {
        onStart { onLoading?.invoke(true) }
            .onEach {
                onSuccess?.invoke(it)
            }
            .catch {
                onError?.invoke(it)
            }
            .onCompletion { onLoading?.invoke(false) }
            .launchIn(viewModelScope)
    }

    protected fun <T> Flow<T>.handleResult(
        flow: MutableStateFlow<BaseState<T>>,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) {
        onStart { flow.emit(BaseState.Loading(true)) }
            .onEach {
                flow.emit(BaseState.Success(it))
                onSuccess?.invoke(it)
            }
            .catch {
                flow.emit(BaseState.Error(it))
                onError?.invoke(it)
            }
            .onCompletion { flow.emit(BaseState.Loading(false)) }
            .launchIn(viewModelScope)
    }

    protected fun <T> Flow<T>.handleResult(
        flow: MutableSharedFlow<BaseState<T>>,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) {
        onStart { flow.emit(BaseState.Loading(true)) }
            .onEach {
                flow.emit(BaseState.Success(it))
                onSuccess?.invoke(it)
            }
            .catch {
                flow.emit(BaseState.Error(it))
                onError?.invoke(it)
            }
            .onCompletion { flow.emit(BaseState.Loading(false)) }
            .launchIn(viewModelScope)
    }

    protected fun <T> emit(flow: MutableSharedFlow<T>, value: T) {
        viewModelScope.launch {
            flow.emit(value)
        }
    }

    protected fun navigationTo(directions: NavDirections) {
        emit(_navigation, directions)
    }

}