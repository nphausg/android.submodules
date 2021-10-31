/*
 * Created by nphau on 01/11/2021, 00:30
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 20/10/2021, 15:54
 */

package com.nphau.android

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.nphau.android.shared.common.extensions.none
import com.nphau.android.shared.di.SharedDI
import com.nphau.android.shared.libs.LocaleUtils
import com.nphau.android.shared.libs.NetworkUtils
import com.nphau.android.shared.libs.imageloader.GlideLoader
import com.nphau.android.shared.libs.imageloader.ImageLoader
import com.nphau.android.shared.libs.logger.DebugLoggingTree
import com.nphau.android.shared.libs.logger.ReleaseLoggingTree
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

abstract class Launcher : MultiDexApplication(), LifecycleObserver {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        appContext = this

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        // Language setting
        LocaleUtils.initializeWithDefaults(this)

        if (isDebug()) {
            Timber.plant(DebugLoggingTree())
            // Fixing formatting
            RxJavaPlugins.setErrorHandler { throwable ->
                throwable.stackTraceToString()
                    .split("\n")
                    .forEach(Timber::e)
                Thread.currentThread().uncaughtExceptionHandler
                    ?.uncaughtException(Thread.currentThread(), throwable)
            }
        } else {
            Timber.plant(releaseLoggingTree())
        }
        NetworkUtils.initializeWithDefaults(this)
        ImageLoader.getInstance().setImageLoader(GlideLoader())
        // Lifecycle for the whole application process.
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleUtils.initializeWithDefaults(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onApplicationEnterForeground() = none()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun onLifecycleCreate() = none()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun onApplicationEnterBackground() = none()

    abstract fun isDebug(): Boolean

    abstract fun releaseLoggingTree(): ReleaseLoggingTree

    override fun onTerminate() {
        super.onTerminate()
        SharedDI.stopDI()
    }
}