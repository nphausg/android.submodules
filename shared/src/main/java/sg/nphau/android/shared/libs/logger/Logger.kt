/*
 * Created by nphau on 01/11/2021, 00:48
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package sg.nphau.android.shared.libs.logger

import timber.log.Timber

const val PADDING = 54
private fun threadName() = Thread.currentThread().name

fun logError(tag: String, message: String?) {
    message ?: return
    val thread = "[🧵 ${threadName()}]"
    val info = "${message.padEnd(PADDING)} $thread"
    Timber.tag(tag).e(info)
}

fun logError(tag: String, obj: Any?) {
    obj ?: return
    val thread = "[🧵 ${threadName()}]"
    val info = "${obj.toString().padEnd(PADDING)} $thread"
    Timber.tag(tag).e(info)
}

fun logError(tag: String, throwable: Throwable?) {
    throwable ?: return
    val thread = "[🧵 ${threadName()}]"
    val message = throwable.message ?: ""
    val info = "${message.padEnd(PADDING)} $thread"
    Timber.tag(tag).e(info)
}

// region LogInfo
fun logInfo(tag: String, message: String?) {
    message ?: return
    val thread = "[🧵 ${threadName()}]"
    val info = "${message.padEnd(PADDING)} $thread"
    Timber.tag(tag).i(info)
}

fun logInfo(tag: String, obj: Any?) {
    obj ?: return
    val thread = "[🧵 ${threadName()}]"
    val info = "${obj.toString().padEnd(PADDING)} $thread"
    Timber.tag(tag).i(info)
}
// endregion