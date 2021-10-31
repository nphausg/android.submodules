/*
 * Created by FS on 4/18/21 9:23 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 4/18/21 9:22 AM
 */

package com.nphau.android.shared.common.extensions

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nphau.android.shared.common.delegates.FragmentBundleDelegate
import java.io.Serializable
import kotlin.properties.ReadWriteProperty

/**
 * Extension method to provide hide keyboard for [Fragment].
 */

inline fun <reified T> FragmentManager.findFragmentByTag(): T? {
    return findFragmentByTag(T::class.java.name) as? T
}

fun FragmentManager.removeFragment(fragment: Fragment?): Boolean {
    if (!isDestroyed && fragment != null) {
        inTransaction { remove(fragment) }
        return true
    }
    return false
}

inline fun <reified T : Fragment> FragmentManager.removeFragmentByTag() {
    if (!isDestroyed) {
        findFragmentByTag<T>()?.let { removeFragment(it) }
    }
}

fun <V> Map<String, V>.toBundle(bundle: Bundle = Bundle()): Bundle = bundle.apply {
    forEach {
        put(it.key, it.value)
    }
}

inline fun FragmentManager.inTransaction(
    allowStateLoss: Boolean = true,
    func: FragmentTransaction.() -> FragmentTransaction
) {
    if (!isStateSaved) {
        beginTransaction().func().commit()
    } else if (allowStateLoss) {
        beginTransaction().func().commitAllowingStateLoss()
    }
}

fun Fragment.isSafeEnough(): Boolean {
    return context != null && isVisible
}

fun <T> Bundle.put(key: String, value: T) {
    when (value) {
        is IBinder -> putBinder(key, value)
        is Bundle -> putBundle(key, value)
        is Byte -> putByte(key, value)
        is ByteArray -> putByteArray(key, value)
        is Char -> putChar(key, value)
        is CharArray -> putCharArray(key, value)
        is CharSequence -> putCharSequence(key, value)
        is Float -> putFloat(key, value)
        is FloatArray -> putFloatArray(key, value)
        is Parcelable -> putParcelable(key, value)
        is Serializable -> putSerializable(key, value)
        is Short -> putShort(key, value)
        is ShortArray -> putShortArray(key, value)
        else -> throw IllegalStateException("Type of property $key is not supported")
    }
}

fun <T : Any> argument(): ReadWriteProperty<Fragment, T> = FragmentBundleDelegate()

fun Fragment.makeDial(number: String) {
    requireContext().makeDial(number)
}