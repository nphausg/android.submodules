/*
 * Created by nphau on 01/11/2021, 00:30
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 26/10/2021, 20:23
 */

package com.nphau.android.shared

import com.nphau.android.Launcher
import com.nphau.android.shared.common.flags.OnLifecycleStart
import com.nphau.android.shared.common.flags.OnLifecycleStop
import com.nphau.android.shared.libs.logger.logInfo
import org.koin.core.component.KoinComponent

/**
 *
 * */
interface PluginInitializer : KoinComponent {

    /**
     * Initializes a module given the application Context
     */
    fun initialize(application: Launcher) {
        log(getTag(), "Initializing ...")
    }

    @OnLifecycleStop
    fun onEnterBackground() {
        log(getTag(), "onEnterBackground ...")
    }

    @OnLifecycleStart
    fun onEnterForeground() {
        log(getTag(), "onEnterForeground ...")
    }

    /**
     * A log function that use 3rd to log
     * @see <a href="https://github.com/JakeWharton/timber">Timber</a>
     */
    fun log(tag: String, message: String?) = logInfo(tag, message)

    fun getTag(): String
}