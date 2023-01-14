/*
 * Created by nphau on 01/11/2021, 00:48
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package sg.nphau.android.shared.libs.logger

import android.os.Build
import android.util.Log
import timber.log.Timber

class DebugLoggingTree : Timber.DebugTree() {

    companion object {
        @JvmStatic
        val BLACK_LIST_OF_DEVICES = arrayListOf("HUAWEI", "samsung")
    }

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {

        val thread = "[🧵 ${Thread.currentThread().name}]"
        val info = "${message.padEnd(54)} $thread"

        // Workaround for devices that doesn't show lower priority logs
        if (BLACK_LIST_OF_DEVICES.any { it == Build.MANUFACTURER }) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
                super.log(Log.ERROR, tag, info, throwable)
            else
                super.log(priority, tag, info, throwable)
        } else {
            super.log(priority, tag, info, throwable)
        }
    }

}
