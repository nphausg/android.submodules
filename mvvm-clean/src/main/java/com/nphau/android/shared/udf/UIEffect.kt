/*
 * Created by nphau on 01/11/2021, 00:47
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package com.nphau.android.shared.udf

import com.nphau.android.shared.common.functional.unit

/**
 * UI-EFFECT: Business logic sends to UI.
 * */
sealed class UIEffect {
    data class Loading(
        val isLoading: Boolean = false,
        val command: UICommand
    ) : UIEffect()

    data class Error(
        val throwable: Throwable,
        val command: UICommand
    ) : UIEffect() {

        override fun toString(): String {
            return throwable.message ?: ""
        }
    }

    override fun toString(): String {
        return if (this is Error) {
            this.throwable.message ?: ""
        } else {
            super.toString()
        }
    }

    inline fun <reified T> isLoadingBy(callBack: unit<Boolean>) {
        if (this is Loading && this.command is T) {
            callBack.invoke(this.isLoading)
        }
    }

    fun isLoading(callBack: unit<Pair<UICommand, Boolean>>) {
        if (this is Loading)
            callBack.invoke(Pair(this.command, this.isLoading))
    }

    fun isError(callBack: unit<Pair<UICommand, String>>) {
        if (this is Error)
            callBack.invoke(Pair(this.command, this.toString()))
    }

    inline fun <reified T> isErrorBy(callBack: unit<String>) {
        if (this is Error && this.command is T)
            callBack.invoke(this.toString())
    }

}