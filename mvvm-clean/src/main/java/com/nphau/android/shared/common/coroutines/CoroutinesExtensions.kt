package com.nphau.android.shared.common.coroutines

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking

fun <T : Any, R : Any> Flowable<T>.suspendMap(transform: suspend (T) -> R): Flowable<R> {
    return this
        .map { item -> runBlocking { transform(item) } }
        .subscribeOn(Schedulers.computation())
}

fun <T : Any, R : Any> Single<T>.suspendMap(transform: suspend (T) -> R): Single<R> {
    return this
        .map { item -> runBlocking { transform(item) } }
        .subscribeOn(Schedulers.computation())
}

fun <T> MutableSharedFlow<T>.doEmit(value: T) {
    check(tryEmit(value))
}