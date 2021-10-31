/*
 * Created by nphau on 01/11/2021, 00:51
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package com.nphau.android.shared.di

import android.app.Application
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.nphau.android.shared.common.extensions.ioDispatcher
import com.nphau.android.shared.common.extensions.mainDispatcher
import com.nphau.android.shared.data.network.NetworkConfig
import com.nphau.android.shared.libs.storage.DataStoreImpl
import com.nphau.android.shared.libs.storage.DataStoreProvider
import com.nphau.android.shared.libs.storage.StringProviderImpl
import com.nphau.android.shared.libs.storage.StringProvider
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Job
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class SchedulersContext { IO }
enum class CoroutinesContext { Main, IO }

object SharedDI {

    fun startDI(
        launcher: Application,
        enableLogs: Boolean = false,
        modules: List<Module> = emptyList()
    ): KoinApplication {
        val koinApplication = startKoin {
            androidContext(launcher)
            if (enableLogs)
                androidLogger(Level.DEBUG)
            modules(modules + shareModule + platformModule(enableLogs))
        }
        val koin = koinApplication.koin
        (koin.get<() -> Unit>()).invoke()
        return koinApplication
    }

    private val shareModule = module {
        // Coroutines
        factory(named(CoroutinesContext.IO)) { Job() + ioDispatcher() }
        factory(named(CoroutinesContext.Main)) { Job() + mainDispatcher() }
        // Scheduler
        factory<Scheduler>(named(SchedulersContext.IO)) { Schedulers.io() }
    }

    private val platformModule = { enableLogs: Boolean ->
        module {
            single { NetworkConfig.httpClient(enableLogs) }
            single<StringProvider> { StringProviderImpl(androidContext()) }
            single<DataStoreProvider> { DataStoreImpl(androidContext()) }
            single { androidContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager }
            single { androidContext().applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }
            single { androidContext().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
        }
    }

    fun stopDI() = stopKoin()
}