/*
 * Created by nphau on 01/11/2021, 00:46
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package com.nphau.android.shared.ui.activities

import android.content.Context
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.nphau.android.R
import com.nphau.android.databinding.ActivityWebviewBinding
import com.nphau.android.shared.common.extensions.clearTop
import com.nphau.android.shared.common.extensions.gone
import com.nphau.android.shared.common.extensions.intentFor
import com.nphau.android.shared.common.extensions.visible
import com.nphau.android.shared.libs.NetworkUtils

class WebViewActivity : BindingActivity<ActivityWebviewBinding>() {

    companion object {

        private const val KEY_URL = "KEY_URL"
        private const val KEY_TITLE = "KEY_TITLE"

        fun start(context: Context, url: String, title: String? = null) {
            context.startActivity(
                context.intentFor<WebViewActivity>(
                    KEY_URL to url,
                    KEY_TITLE to title
                ).clearTop()
            )
        }
    }

    override fun onSyncViews(savedInstanceState: Bundle?) {
        super.onSyncViews(savedInstanceState)
        with(binding) {
            webView.webChromeClient = BrowserWebClient()
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }
            }
        }
    }

    override fun onSyncData() {
        super.onSyncData()
        val url = intent.getStringExtra(KEY_URL) ?: "https://nphau.medium.com/"
        val title = intent.getStringExtra(KEY_TITLE) ?: getString(R.string.common_owner)
        with(binding) {
            toolBar.title = title
            toolBar.setNavigationOnClickListener { finish() }
            if (NetworkUtils.isNetworkAvailable()) {
                webView.loadUrl(url)
                viewEmpty.gone()
            } else {
                viewEmpty.visible()
            }
        }
    }
}

internal class BrowserWebClient : WebChromeClient() {

}
