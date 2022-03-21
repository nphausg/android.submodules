/*
 * Created by nphau on 01/11/2021, 00:46
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package sg.nphau.android.shared.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import sg.nphau.android.R
import sg.nphau.android.shared.common.extensions.toHTML
import sg.nphau.android.shared.common.extensions.inTransaction
import sg.nphau.android.shared.common.extensions.longToast
import sg.nphau.android.shared.libs.CommonUtils
import sg.nphau.android.shared.libs.NetworkUtils
import sg.nphau.android.shared.ui.UIBehaviour
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

open class SharedFragment : Fragment(), UIBehaviour {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerBackPressed()
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
     * Note that you could enable/disable the callback here as
     * well by setting callback.isEnabled = true/false
     */
    private fun registerBackPressed() {
        /** true means that the callback is enabled */
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(enableOnBackPressed()) {
                override fun handleOnBackPressed() = onBackPressed()
            })
    }

    protected open fun onBackPressed() = Unit

    protected open fun enableOnBackPressed() = false

    override fun showError(message: String?) {
        var messageNeedToBeShow = message ?: ""
        if (message.isNullOrEmpty()) {
            messageNeedToBeShow = if (NetworkUtils.isNetworkAvailable())
                getString(R.string.common_error_unknown)
            else
                getString(R.string.common_error_connect)
        }
        requireContext().longToast(messageNeedToBeShow.toHTML().toString())
    }

    override fun makeVibrator() = CommonUtils.makeVibrator(requireContext())

    open fun killMySelf() {
        parentFragmentManager.inTransaction {
            remove(this@SharedFragment)
        }
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
