/*
 * Created by nphau on 01/11/2021, 00:49
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package com.nphau.android.shared.common.extensions

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function
import java.util.concurrent.TimeUnit
import kotlin.math.pow

/**
 * This observable timeline would be:
 * 0:00 Subscription takes place
 * 0:02 emits "0L" after 2 seconds
 * 0:04 emits "0L" after 4 seconds
 * 0:08 emits "0L" after 8 seconds
 **/
class RetryWithPower2Delay(private val maxRetries: Int) :
    Function<Observable<out Throwable>, Observable<*>> {

    override fun apply(errors: Observable<out Throwable>): Observable<*> {
        return errors.zipWith(
            Observable.range(1, maxRetries + 1),
            { error: Throwable, retryCount: Int ->
                if (retryCount > maxRetries) {
                    throw error
                } else {
                    retryCount
                }
            }
        ).flatMap { retryCount: Int -> timer(retryCount) }
    }

    companion object {
        private val timer = { retryCount: Int ->
            Observable.timer(
                2.toDouble().pow(retryCount.toDouble()).toLong(),
                TimeUnit.SECONDS
            )
        }
    }

}
