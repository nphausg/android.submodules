/*
 * Created by nphau on 01/11/2021, 00:47
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package sg.nphau.android.shared.udf

/**
 * UI-COMMAND: View sends to business logic
 * */
interface UICommand

inline fun <reified T> UICommand.ofType(callBack: T.() -> Unit) {
    if (this is T)
        callBack(this)
}