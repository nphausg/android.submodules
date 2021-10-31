/*
 * Created by nphau on 01/11/2021, 00:49
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package com.nphau.android.shared.common.extensions

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.nphau.android.R
import com.nphau.android.shared.libs.imageloader.ImageLoader
import com.nphau.android.shared.ui.activities.BindingActivity
import com.nphau.android.shared.ui.fragments.BindingFragment
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.Disposable
import java.lang.reflect.ParameterizedType
import java.util.concurrent.TimeUnit

// region [Binding]
internal fun Class<*>.checkMethod(): Boolean {
    return try {
        getMethod(
            "inflate",
            LayoutInflater::class.java
        )
        true
    } catch (ex: Exception) {
        false
    }
}

internal fun Any.findClass(): Class<*> {
    var javaClass: Class<*> = this.javaClass
    var result: Class<*>? = null
    while (result == null || !result.checkMethod()) {
        result = (javaClass
            .genericSuperclass as? ParameterizedType)
            ?.actualTypeArguments?.firstOrNull {
                if (it is Class<*>) {
                    it.checkMethod()
                } else {
                    false
                }
            } as? Class<*>
        javaClass = javaClass.superclass
    }
    return result
}

internal fun <V : ViewBinding> Class<*>.getBinding(
    layoutInflater: LayoutInflater,
    container: ViewGroup?
): V {
    return try {
        @Suppress("UNCHECKED_CAST")
        getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        ).invoke(null, layoutInflater, container, false) as V
    } catch (ex: Exception) {
        throw RuntimeException("The ViewBinding inflate function has been changed.", ex)
    }
}

internal fun <V : ViewBinding> Class<*>.getBinding(layoutInflater: LayoutInflater): V {
    return try {
        @Suppress("UNCHECKED_CAST")
        getMethod(
            "inflate",
            LayoutInflater::class.java
        ).invoke(null, layoutInflater) as V
    } catch (ex: Exception) {
        throw RuntimeException("The ViewBinding inflate function has been changed.", ex)
    }
}

internal fun <V : ViewBinding> BindingActivity<V>.getBinding(): V {
    return findClass().getBinding(layoutInflater)
}

internal fun <V : ViewBinding> BindingFragment<V>.getBinding(
    inflater: LayoutInflater, container: ViewGroup?
): V = findClass().getBinding(inflater, container)

/* Used for ViewBinding
* - parent: ViewGroup
* - attachToRoot: Boolean
* - inflater: LayoutInflater
* */
fun <V : ViewBinding> ViewGroup.inflate(
    factory: (LayoutInflater, ViewGroup, Boolean) -> V,
    attachToParent: Boolean = false
) = factory(LayoutInflater.from(context), this, attachToParent)

// Used for DataBinding
fun <V : ViewBinding> ViewGroup.inflate(layoutId: Int, attachToParent: Boolean = false): V =
    DataBindingUtil.inflate(
        LayoutInflater.from(context),
        layoutId, this, attachToParent
    )

// endregion

inline fun View.safeClick(crossinline block: () -> Unit): Disposable =
    RxView.clicks(this)
        .throttleFirst(850, TimeUnit.MILLISECONDS)
        .subscribe({ block() }, { })

fun View?.visible() {
    if (this != null) {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
    }
}

fun View?.gone() {
    if (this != null) {
        if (visibility != View.GONE) {
            visibility = View.GONE
        }
    }
}

@BindingAdapter("invisibleUnless")
fun View.invisibleUnless(conditionToVisible: Boolean? = true) {
    visibility = if (conditionToVisible == null || conditionToVisible == true) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter("goneUnless")
fun View.goneUnless(conditionToVisible: Boolean? = true) {
    visibility = if (conditionToVisible == null || conditionToVisible == true) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun RecyclerView.defaultDivider() = setDivider(
    orientation = DividerItemDecoration.VERTICAL,
    dividerResId = R.drawable.all_divider_soft_line
)

fun RecyclerView.setDivider(
    orientation: Int = DividerItemDecoration.VERTICAL, dividerResId: Int? = null
) {
    val dividerItemDecoration = DividerItemDecoration(this.context, orientation)
    dividerResId?.let { resId ->
        this.context.getDrawableCompat(resId)
            ?.let { divider ->
                dividerItemDecoration.setDrawable(divider)
            }
    }
    this.addItemDecoration(dividerItemDecoration)
}

fun <T> ImageView.load(resource: T, overrideSize: Int = -1) {
    ImageLoader.getInstance().load(this, resource, overrideSize)
}

val Float.px: Float get() = (this * Resources.getSystem().displayMetrics.density)
val Float.dp: Float get() = (this / Resources.getSystem().displayMetrics.density)

// region [ViewPager2]
fun ViewPager2.onPageSelected(onPageSelected: (position: Int) -> Unit = { _ -> }) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onPageSelected(position)
        }
    })
}
// endregion