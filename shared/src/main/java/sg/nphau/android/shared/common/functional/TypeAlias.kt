/*
 * Created by nphau on 21/03/2022, 00:30
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 21/03/2022, 20:23
 */

package sg.nphau.android.shared.common.functional

import android.view.LayoutInflater
import android.view.ViewGroup

typealias unit<T> = (T) -> Unit

// CallBack
typealias CallBack = () -> Unit

fun CallBack.call() = this()
fun <T> consume(result: T, block: CallBack): T {
    block.call()
    return result
}
// Transformer
typealias Transformer<I, O> = (I) -> O

typealias Condition = () -> Boolean
typealias Builder<T> = T.() -> Unit
typealias Predicate<T> = (T) -> Boolean

// Provider
typealias Provider<T> = () -> T

fun <R> Provider<R>.get() = this()

/**
 * Pass any exception while processing block
 * When use this method meaning statement no need to handle exception, exception now not critical
 * @return last statement result or null if throw any exceptions
 */
fun <T> tryOrNull(provider: Provider<T>) = try {
    provider.get()
} catch (e: Throwable) {
    null
}

typealias BindingInflater<VB> = (LayoutInflater, ViewGroup?, Boolean) -> VB