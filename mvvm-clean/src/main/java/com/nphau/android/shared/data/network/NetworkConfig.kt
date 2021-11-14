package com.nphau.android.shared.data.network

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object NetworkConfig {

    private const val WRITE_TIMEOUT = 60L
    private const val READ_TIMEOUT = 60L
    private const val CONNECT_TIMEOUT = 60L

    private val engine = { enableLogs: Boolean ->
        OkHttp.create {
            config {
                followRedirects(true)
                followSslRedirects(true)
                retryOnConnectionFailure(true)
                writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                if (enableLogs) {
                    val loggingInterceptor = HttpLoggingInterceptor()
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(loggingInterceptor)
                }
            }
        }
    }

    val httpClient = { enableLogs: Boolean ->
        HttpClient(engine(enableLogs)) {
            install(HttpTimeout) {
                connectTimeoutMillis = CONNECT_TIMEOUT * 1000
                socketTimeoutMillis = CONNECT_TIMEOUT * 1000
                requestTimeoutMillis = READ_TIMEOUT * 1000
            }
            install(JsonFeature) {
                serializer = GsonSerializer() {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                }
            }
        }
    }

}
