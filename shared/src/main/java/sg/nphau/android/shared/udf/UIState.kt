/*
 * Created by nphau on 01/11/2021, 00:47
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package sg.nphau.android.shared.udf

/**
 * UI-STATE: Immutable ViewState attributes
 * */
interface UIState

object EmptyState : UIState

inline fun <reified T> UIState.ofType(callBack: T.() -> Unit) {
    if (this is T)
        callBack(this)
}

inline fun <reified T> UIState.ofType(onYes: T.() -> Unit, onNo: () -> Unit) {
    if (this is T)
        onYes(this)
    else
        onNo()
}