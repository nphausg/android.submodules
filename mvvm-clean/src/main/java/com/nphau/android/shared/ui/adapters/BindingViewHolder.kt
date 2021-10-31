/*
 * Created by nphau on 01/11/2021, 00:46
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package com.nphau.android.shared.ui.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.nphau.android.shared.ui.BindingDSL

/**
 * A generic ViewHolder that works with a [ViewBinding].
 * @param <T> The type of the ViewBinding.
</T> */
abstract class BindingViewHolder<VB : ViewBinding>(
    @BindingDSL
    protected open val binding: VB
) : RecyclerView.ViewHolder(binding.root) {

    protected fun context(): Context = itemView.context

}
