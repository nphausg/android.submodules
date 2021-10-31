/*
 * Created by nphau on 01/11/2021, 00:47
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package com.nphau.android.shared.libs

import android.content.Context
import com.nphau.android.shared.common.extensions.asActivityManager

object DeviceUtils {

    private const val OPTIMUM_CORE = 4
    private const val OPTIMUM_MEMORY_MB = 124

    fun isHighPerformingDevice(context: Context): Boolean {
        val activityManager = context.asActivityManager()
        return !activityManager.isLowRamDevice &&
                Runtime.getRuntime().availableProcessors() >= OPTIMUM_CORE &&
                activityManager.memoryClass >= OPTIMUM_MEMORY_MB
    }

}