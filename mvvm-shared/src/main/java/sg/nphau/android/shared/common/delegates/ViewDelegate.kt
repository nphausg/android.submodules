/*
 * Created by nphau on 01/11/2021, 00:50
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:50
 */

package sg.nphau.android.shared.common.delegates

import android.widget.TextView
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun TextView.text(): ReadWriteProperty<Any, String> =
    object : ReadWriteProperty<Any, String> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ): String = text.toString()

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>, value: String
        ) {
            text = value
        }
    }
