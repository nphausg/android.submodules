/*
 * Created by nphau on 01/11/2021, 00:46
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package sg.nphau.android.shared.ui.activities

import android.content.Context
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import sg.nphau.android.databinding.ActivityWebviewBinding
import sg.nphau.android.shared.common.extensions.clearTop
import sg.nphau.android.shared.common.extensions.gone
import sg.nphau.android.shared.common.extensions.intentFor
import sg.nphau.android.shared.common.extensions.visible
import sg.nphau.android.shared.libs.NetworkUtils

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
            with(webView) {
                webChromeClient = BrowserWebClient()
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        view.loadUrl(url)
                        return true
                    }
                }
                isVerticalScrollBarEnabled = true
                isHorizontalScrollBarEnabled = true
            }

            // Settings
            with(webView.settings) {
                defaultTextEncodingName = "utf-8"
                javaScriptEnabled = true
                loadsImagesAutomatically = true
                setSupportZoom(true)
                useWideViewPort = false
                builtInZoomControls = true
                loadWithOverviewMode = true
                displayZoomControls = false

                // Storage
                domStorageEnabled = true
                allowFileAccess = true
            }
        }
    }

    override fun onSyncData() {
        super.onSyncData()
        val url = intent.getStringExtra(KEY_URL) ?: "https://nphau.medium.com/"
        val title = intent.getStringExtra(KEY_TITLE) ?: ""
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
