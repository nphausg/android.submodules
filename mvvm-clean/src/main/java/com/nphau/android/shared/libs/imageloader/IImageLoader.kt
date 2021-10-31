/*
 * Created by nphau on 01/11/2021, 00:48
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package com.nphau.android.shared.libs.imageloader

import android.widget.ImageView

interface IImageLoader {

    fun <T> load(imageView: ImageView, resource: T)

    fun <T> load(imageView: ImageView, resource: T, overrideSize: Int)

}