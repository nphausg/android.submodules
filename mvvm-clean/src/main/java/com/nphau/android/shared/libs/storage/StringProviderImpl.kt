/*
 * Created by nphau on 01/11/2021, 00:48
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package com.nphau.android.shared.libs.storage

import android.content.Context
import androidx.annotation.StringRes

class StringProviderImpl constructor(private val context: Context) : StringProvider {

    override fun getString(@StringRes resId: Int, vararg param: Any): String =
        context.resources.getString(resId, *param)

}