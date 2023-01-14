/*
 * Created by nphau on 21/03/2022, 00:30
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 21/03/2022, 20:23
 */

package sg.nphau.android.shared

import sg.nphau.android.Launcher
import sg.nphau.android.shared.common.flags.OnLifecycleStart
import sg.nphau.android.shared.common.flags.OnLifecycleStop
import sg.nphau.android.shared.libs.logger.logInfo
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