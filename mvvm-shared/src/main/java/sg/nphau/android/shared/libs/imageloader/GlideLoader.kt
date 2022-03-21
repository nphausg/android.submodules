/*
 * Created by nphau on 01/11/2021, 00:48
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package sg.nphau.android.shared.libs.imageloader

import android.widget.ImageView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import sg.nphau.android.R

class GlideLoader : IImageLoader {

    override fun <T> load(imageView: ImageView, resource: T) {
        GlideApp.with(imageView.context)
            .load(resource)
            .apply(RequestOptions.placeholderOf(R.drawable.all_background_thumbnail))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .apply(RequestOptions.priorityOf(Priority.IMMEDIATE))
            .apply(RequestOptions.errorOf(R.drawable.all_background_thumbnail))
            .into(imageView)
    }

    override fun <T> load(imageView: ImageView, resource: T, overrideSize: Int) {
        if (overrideSize > 0)
            GlideApp.with(imageView.context)
                .load(resource)
                .apply(RequestOptions.overrideOf(overrideSize))
                .apply(RequestOptions.placeholderOf(R.drawable.all_background_thumbnail))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.priorityOf(Priority.IMMEDIATE))
                .apply(RequestOptions.errorOf(R.drawable.all_background_thumbnail))
                .into(imageView)
        else
            load(imageView, resource)
    }
}