/*
 * Created by FS on 4/18/21 9:23 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 4/18/21 9:22 AM
 */

package com.nphau.android.shared.common.extensions

import android.content.res.TypedArray
import android.text.Spanned
import androidx.core.text.HtmlCompat

/**
 * Just empty function, do not ask me why
 * @since 1.0.0
 */
fun none() = Unit

inline fun <reified T : Any> T.tag(): String = T::class.java.simpleName

inline fun <T : TypedArray?, R> T.use(block: (T) -> R): R {
    var recycled = false
    try {
        return block(this)
    } catch (e: Exception) {
        recycled = true
        try {
            this?.recycle()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw e
    } finally {
        if (!recycled) {
            this?.recycle()
        }
    }
}

fun String.toHTML(): Spanned {
    if (!isNullOrEmpty()) {
        if (contains("\n"))
            return HtmlCompat
                .fromHtml(replace("\n", "<br/>"), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
    return HtmlCompat
        .fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun String.lastWord(): String {
    if (isNotEmpty()) {
        val arrayOfWords = split(" ")
        if (arrayOfWords.isNotEmpty())
            return arrayOfWords[arrayOfWords.size - 1]
    }
    return ""
}

infix fun <T> T?.or(default: T): T = this ?: default