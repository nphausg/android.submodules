/*
 * Created by nphau on 01/11/2021, 00:49
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package sg.nphau.android.shared.common.extensions

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

// region [CompositeDisposable]
operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

fun completable(block: (CompletableEmitter) -> Unit): Completable = Completable.create(block)

fun <T> completableFromCallable(block: () -> T): Completable = Completable.fromCallable(block)

fun Completable.subscribeIgnoringResult(): Disposable = subscribe({}, {})

// endregion

// region [Observable]

fun <T> observable(block: (ObservableEmitter<T>) -> Unit): Observable<T> = Observable.create(block)

fun <T> observableFromCallable(block: () -> T): Observable<T> = Observable.fromCallable(block)

fun <T> Observable<T>.subscribeIgnoringResult(): Disposable = subscribe({}, {})

// endregion

// region [Flowable]
fun <T> Flowable<T>.subscribeIgnoringResult(): Disposable = subscribe({}, {})

// endregion

//region [Single]
inline fun <reified T> Single<T>.applyScheduler(scheduler: Scheduler): Single<T> =
    subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())

inline fun <reified T> applySingleIoScheduler(): SingleTransformer<T, T> {
    return SingleTransformer { single -> single.applyScheduler(Schedulers.io()) }
}

fun <T> single(block: (SingleEmitter<T>) -> Unit): Single<T> = Single.create(block)
fun <T> singleFromCallable(block: () -> T): Single<T> = Single.fromCallable(block)

fun <T> Single<T>.subscribeIgnoringResult(): Disposable = subscribe({}, {})

// endregion

fun <T> Maybe<T>.subscribeIgnoringResult(): Disposable = subscribe({}, {})
// endregion

fun interval(mIntervalInMillis: Long): Flowable<Long> = Flowable
    .interval(mIntervalInMillis, TimeUnit.MILLISECONDS)
    .observeOn(AndroidSchedulers.mainThread())