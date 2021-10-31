/*
 * Created by nphau on 01/11/2021, 00:47
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package com.nphau.android.shared.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.nphau.android.R
import com.nphau.android.shared.common.extensions.toHTML
import com.nphau.android.shared.common.extensions.longToast
import com.nphau.android.shared.libs.CommonUtils
import com.nphau.android.shared.libs.LocaleUtils
import com.nphau.android.shared.libs.NetworkUtils
import com.nphau.android.shared.ui.UIBehaviour
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

open class SharedActivity : FragmentActivity(), UIBehaviour {

    // User for language setting
    override fun attachBaseContext(newBase: Context?) {
        LocaleUtils.self().setLocale(newBase)?.let {
            super.attachBaseContext(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpBinding()
        onSyncViews(savedInstanceState)
        onSyncEvents()
        onSyncData()
    }

    override fun onSyncViews(savedInstanceState: Bundle?) {

    }

    override fun onSyncEvents() {

    }

    override fun onSyncData() {

    }

    /**
     * Used for setUp binding
     */
    internal open fun setUpBinding() = Unit

    override fun showError(message: String?) {
        var messageNeedToBeShow = message ?: ""
        if (message.isNullOrEmpty()) {
            messageNeedToBeShow = if (NetworkUtils.isNetworkAvailable())
                getString(R.string.common_error_unknown)
            else
                getString(R.string.common_error_connect)
        }
        longToast(messageNeedToBeShow.toHTML().toString())
    }

    override fun makeVibrator() = CommonUtils.makeVibrator(this)

    override fun startActivity(intent: Intent?) {
        try {
            super.startActivity(intent)
            overridePendingTransitionEnter()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        try {
            super.startActivityForResult(intent, requestCode)
            overridePendingTransitionEnter()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransitionExit()
    }

    private fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.anim_slide_from_right, R.anim.anim_slide_to_left)
    }

    private fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.anim_slide_from_left, R.anim.anim_slide_to_right)
    }

    open fun allowUserDismissKeyboardWhenClickOutSide() = false

    override fun dispatchTouchEvent(motionEvent: MotionEvent?): Boolean {
        if (allowUserDismissKeyboardWhenClickOutSide()) {
            if (currentFocus != null)
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(motionEvent)
    }

    // region Coroutine
    protected fun <T> on(flow: SharedFlow<T>, action: suspend (T) -> Unit) {
        flow.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach(action)
            .launchIn(lifecycleScope)
    }

    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch(Dispatchers.IO, CoroutineStart.DEFAULT, block)
    }

    protected fun launchWhenStarted(block: suspend CoroutineScope.() -> Unit) =
        lifecycleScope.launchWhenStarted(block)

    protected fun launchWhenResumed(block: suspend CoroutineScope.() -> Unit) =
        lifecycleScope.launchWhenResumed(block)

    protected fun launchWhenCreated(block: suspend CoroutineScope.() -> Unit) =
        lifecycleScope.launchWhenCreated(block)
    // endregion
}
