/*
 * Created by nphau on 01/11/2021, 00:45
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package com.nphau.android.shared.ui

import android.os.Bundle

interface UIBehaviour {

    fun showError(message: String?)

    fun onSyncViews(savedInstanceState: Bundle?)

    fun onSyncEvents()

    fun onSyncData()

    fun makeVibrator()

}
