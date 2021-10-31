/*
 * Created by nphau on 01/11/2021, 00:46
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package com.nphau.android.shared.ui.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.nphau.android.shared.common.extensions.mainThread

@DslMarker
internal annotation class AdapterBuilderDSL

/**
 *
 * */
open class TemplateAdapter<T, VB : ViewBinding>(
    private val onCreate: ViewGroup.() -> VB,
    private val onBind: (RowView<T, VB>, TemplateAdapter<T, VB>) -> Unit,
    override val compare: (T, T) -> Boolean = { _: T, _: T -> false }
) : SharedAdapter<T>(compare) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TemplateViewHolder(onCreate(parent))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TemplateAdapter<*, *>.TemplateViewHolder).bind(position)
    }

    inner class TemplateViewHolder(override val binding: VB) : BindingViewHolder<VB>(binding) {

        fun bind(position: Int) = mainThread {
            onBind(
                RowView(
                    row = Pair(binding, get(position)),
                    view = Triple(itemView, position, itemCount)
                ),
                this@TemplateAdapter
            )
        }

    }

}

/**
 * A RowView container
 * @property row includes: ViewBinding, Model T
 * @property view includes: [View, position, itemCount]
 */
data class RowView<T, VB : ViewBinding>(
    val row: Pair<VB, T>,
    val view: Triple<View, Int, Int>
) {

    fun hasDivider() = view.second != view.third - 1

    fun context(): Context = view.first.context

}

@AdapterBuilderDSL
fun <T, VB : ViewBinding> adapterOf(
    onCreate: ViewGroup.() -> VB,
    onBind: (RowView<T, VB>, TemplateAdapter<T, VB>) -> Unit,
    onCompare: (T, T) -> Boolean = { _: T, _: T -> false }
) = TemplateAdapter(onCreate, onBind, onCompare)