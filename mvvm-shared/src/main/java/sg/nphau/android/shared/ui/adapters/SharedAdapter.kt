/*
 * Created by nphau on 01/11/2021, 00:46
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package sg.nphau.android.shared.ui.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.properties.Delegates

val IO_EXECUTOR: ExecutorService = Executors.newSingleThreadExecutor()

abstract class SharedAdapter<T>(protected open val compare: (T, T) -> Boolean = { _: T, _: T -> false }) :
    ListAdapter<T, RecyclerView.ViewHolder>(
        AsyncDifferConfig.Builder(object : DiffUtil.ItemCallback<T>() {
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
            override fun areItemsTheSame(oldItem: T, newItem: T) = compare(oldItem, newItem)
        }).setBackgroundThreadExecutor(IO_EXECUTOR).build()
    ) {

    // region SingleSelectable

    // This keeps track of the currently selected position
    private var selectedPosition by Delegates.observable(-1) { _, oldPos, newPos ->
        if (newPos in currentList.indices) {
            notifyItemChanged(oldPos)
            notifyItemChanged(newPos)
        }
    }

    fun select(item: T?) {
        selectedPosition = currentList.indexOf(item)
    }

    fun select(index: Int) {
        selectedPosition = index
    }

    fun selectedIndex() = selectedPosition

    /**
     * @return true if this [position] is selected
     * */
    fun isSelected(position: Int): Boolean {
        return position == selectedPosition
    }

    // endregion

    open fun get(position: Int): T = super.getItem(position)

    fun atLeast(threadHold: Int) = itemCount >= threadHold

    fun isEmpty() = itemCount <= 0

}