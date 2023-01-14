/*
 * Created by nphau on 01/11/2021, 00:45
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package sg.nphau.android.shared.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sg.nphau.android.shared.common.extensions.tag
import sg.nphau.android.shared.common.extensions.ioDispatcher
import sg.nphau.android.shared.common.functional.Result
import sg.nphau.android.shared.common.functional.exceptionOrNull
import sg.nphau.android.shared.common.functional.get
import sg.nphau.android.shared.common.functional.unit
import sg.nphau.android.shared.libs.logger.logInfo
import sg.nphau.android.shared.udf.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

/**
 * [SharedViewModel] can also be used as a communication layer between views (Fragments/Activity) and business.
 * Each View can acquire the ViewModel using the same key via their Activity. This allows
 * */
abstract class SharedViewModel<C : UICommand, S : UIState> : ViewModel(), KoinComponent {

    // region UDF
    // Effect
    private val _effect = MutableSharedFlow<UIEffect>()
    val effect = _effect.asSharedFlow()

    // Command
    protected val command = MutableSharedFlow<C>()

    // State
    private val _state = MutableStateFlow<UIState>(EmptyState)
    val state = _state.asStateFlow()

    init { // process command
        launch {
            command.collect {
                logInfo(tag(), "[COMMAND -> ${it::class.simpleName}]")
                onCommand(it)
            }
        }
    }

    // region Command
    /**
     * [onCommand] In this function, the derived class must be override and
     * process itself the [UICommand] which is sent by View
     * */
    protected abstract suspend fun onCommand(command: UICommand)

    open fun command(cmd: C) = launch { command.emit(cmd) }

    protected suspend inline fun <reified T> UICommand.onSuccess(
        result: Result<T, Throwable>, onSuccess: unit<T>
    ) {
        if (result.isSuccess) {
            onSuccess(result.get())
        } else {
            result.exceptionOrNull()?.let { error ->
                effect(UIEffect.Error(error, this))
            }
        }
    }
    // endregion

    // region Effect
    protected suspend fun effect(effect: UIEffect) = _effect.emit(effect)
    // endregion

    // region State
    protected suspend fun changeState(newState: S) = _state.emit(newState)
    protected fun updateState(newState: S) = _state.update { newState }
    inline fun <reified T> isState(callBack: T.() -> Unit) {
        state.value.ofType(callBack)
    }

    inline fun <reified T> isState(onState: T.() -> Unit, onOthers: () -> Unit) {
        state.value.ofType(onState, onOthers)
    }
    // endregion

    // region Utilities
    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(ioDispatcher(), CoroutineStart.DEFAULT, block)
    }

    protected fun launch(command: UICommand, job: suspend CoroutineScope.() -> Unit) =
        launch {
            effect(UIEffect.Loading(true, command))
            job()
            effect(UIEffect.Loading(false, command))
        }
    // endregion
}
