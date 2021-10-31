/*
 * Created by nphau on 01/11/2021, 00:48
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package com.nphau.android.shared.libs.logger

import android.util.Log
import timber.log.Timber

abstract class ReleaseLoggingTree : Timber.Tree() {

    companion object {

        @JvmStatic
        val CRASHLYTICS_KEY_PRIORITY = "priority"

        @JvmStatic
        val CRASHLYTICS_KEY_TAG = "tag"

        @JvmStatic
        val CRASHLYTICS_KEY_MESSAGE = "message"
    }

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {

        applyKeys(priority, tag ?: System.currentTimeMillis().toString(), message)

        if (priority >= Log.ERROR)
            logException(throwable ?: Exception(message))
        else
            logOther(priority, tag, message)
    }

    abstract fun applyKeys(priority: Int, tag: String?, message: String)

    abstract fun logOther(priority: Int, tag: String?, message: String)

    abstract fun logException(throwable: Throwable?)
}
