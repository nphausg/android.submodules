/*
 * Created by nphau on 01/11/2021, 00:50
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package com.nphau.android.shared.common.delegates

import android.app.Activity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityBindingProperty<out T : ViewDataBinding>(@LayoutRes private val resId: Int) :
        ReadOnlyProperty<Activity, T> {

    private var binding: T? = null
    
    override operator fun getValue(thisRef: Activity, property: KProperty<*>): T =
            binding ?: createBinding(thisRef).also { binding = it }

    private fun createBinding(activity: Activity): T = DataBindingUtil.setContentView(activity, resId)
}
