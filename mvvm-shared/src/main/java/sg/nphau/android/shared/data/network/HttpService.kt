/*
 * Created by nphau on 01/11/2021, 00:51
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package sg.nphau.android.shared.data.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

abstract class HttpService {

    protected abstract val httpClient: HttpClient

    protected suspend inline fun <reified R> post(
        url: String, block: HttpRequestBuilder.() -> Unit = {}
    ): R = httpClient.post {
        url { takeFrom(url) }
        apply(block)
    }

    protected suspend inline fun <reified R> get(
        url: String, block: HttpRequestBuilder.() -> Unit = {}
    ): R = httpClient.get {
        url { takeFrom(url) }
        apply(block)
    }
}