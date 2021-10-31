/*
 * Created by FS on 3/25/21 4:05 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 3/25/21 4:05 PM
 */

package com.nphau.android.shared.common.flags

@Target(
    AnnotationTarget.CLASS, AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.EXPRESSION
)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class OnLifecycleCreate