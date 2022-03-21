/*
 * Created by nphau on 01/11/2021, 00:48
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package sg.nphau.android.shared.libs.storage

import androidx.annotation.StringRes

interface StringProvider {
    fun getString(@StringRes resId: Int, vararg param: Any): String
}