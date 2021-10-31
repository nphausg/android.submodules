/*
 * Created by FS on 4/18/21 9:23 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 4/18/21 9:22 AM
 */

package com.nphau.android.shared.common.functional

import androidx.lifecycle.Observer

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
class EventObserver<T>(private val onEventUnhandledContent: unit<T>) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}
