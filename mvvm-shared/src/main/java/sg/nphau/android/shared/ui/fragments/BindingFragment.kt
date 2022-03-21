/*
 * Created by nphau on 01/11/2021, 00:46
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package sg.nphau.android.shared.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import sg.nphau.android.shared.common.extensions.getBinding
import sg.nphau.android.shared.ui.BindingDSL

abstract class BindingFragment<VB : ViewBinding> : SharedFragment() {

    private var _binding: VB? = null

    /**
     * Binding variable to be used for accessing views.
     * @return ViewBinding
     * */
    @BindingDSL
    protected val binding: VB
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getBinding(inflater, container)
        return requireNotNull(_binding).root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}