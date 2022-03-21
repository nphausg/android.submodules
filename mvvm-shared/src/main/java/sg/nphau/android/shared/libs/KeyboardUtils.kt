/*
 * Created by nphau on 01/11/2021, 00:47
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package sg.nphau.android.shared.libs

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import sg.nphau.android.shared.common.functional.tryOrNull

object KeyboardUtils {

    fun hideKeyboardIfNeed(activity: Activity) {
        tryOrNull {
            activity.currentFocus?.let { view ->
                (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    fun showKeyboardIfNeed(view: View) {
        view.isFocusableInTouchMode = true
        if (view.isFocusable) {
            view.requestFocus()
        }
        if (view is EditText) {
            view.setSelection(view.length())
        }
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }
}