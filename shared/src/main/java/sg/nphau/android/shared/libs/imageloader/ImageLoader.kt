/*
 * Created by nphau on 01/11/2021, 00:50
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:49
 */

package sg.nphau.android.shared.libs.imageloader

import android.widget.ImageView
import sg.nphau.android.shared.common.Singleton

class ImageLoader private constructor() : IImageLoader {

    companion object : Singleton<ImageLoader>(::ImageLoader)

    private var loader: IImageLoader = GlideLoader()

    fun setImageLoader(loader: IImageLoader) {
        this.loader = loader
    }

    override fun <T> load(imageView: ImageView, resource: T) {
        this.loader.load(imageView, resource)
    }

    override fun <T> load(imageView: ImageView, resource: T, overrideSize: Int) {
        this.loader.load(imageView, resource, overrideSize)
    }


}