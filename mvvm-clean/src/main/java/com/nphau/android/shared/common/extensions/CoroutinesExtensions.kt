package com.nphau.android.shared.common.extensions

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// region Executors
val IO_EXECUTOR: ExecutorService = Executors.newSingleThreadExecutor()
val MAIN_THREAD = MainThreadExecutor()
fun mainThread(f: () -> Unit) {
    MAIN_THREAD.execute(f)
}

fun ioThread(f: () -> Unit) {
    IO_EXECUTOR.execute(f)
}

class MainThreadExecutor : Executor {
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    override fun execute(command: Runnable) {
        mainThreadHandler.post(command)
    }
}
// endregion

// region Coroutines
fun <T> runBlocking(block: suspend () -> T): T =
    kotlinx.coroutines.runBlocking { block.invoke() }

fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main
val mainScope = CoroutineScope(Job() + mainDispatcher())
fun mainScope(f: suspend CoroutineScope.() -> Unit) {
    mainScope.launch(mainDispatcher(), CoroutineStart.DEFAULT, f)
}

fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO
val ioScope = CoroutineScope(Job() + ioDispatcher())
fun ioScope(f: suspend CoroutineScope.() -> Unit) {
    ioScope.launch(ioDispatcher(), CoroutineStart.DEFAULT, f)
}
// endregion
