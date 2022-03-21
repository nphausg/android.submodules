/*
 * Created by nphau on 01/11/2021, 00:45
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package sg.nphau.android.shared.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import sg.nphau.android.R
import sg.nphau.android.databinding.ViewEmptyBinding
import sg.nphau.android.shared.common.extensions.getStyledAttributes
import sg.nphau.android.shared.common.extensions.inflate

class EmptyView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private val binding = inflate(ViewEmptyBinding::inflate, true)

    init {

        context.getStyledAttributes(attributeSet, R.styleable.EmptyView) {
            setMessage(getString(R.styleable.EmptyView_f_message))
        }

    }

    fun setMessage(message: String?) {
        binding.textMessage.text = message
    }
}