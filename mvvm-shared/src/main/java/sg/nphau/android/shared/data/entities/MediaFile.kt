/*
 * Created by nphau on 01/11/2021, 00:51
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package sg.nphau.android.shared.data.entities

import android.net.Uri

data class MediaFile(
    var id: Long = -1,
    var contentUri: Uri? = null,
    val folderName: String = "",
    var title: String = "",
    var description: String = "",
    var extension: String = "",
    val files: List<MediaFile> = emptyList(),
    val mediaType: MediaType = MediaType.IMAGE,
) {

    fun isImageType() = mediaType == MediaType.IMAGE

}

enum class MediaType { VIDEO, IMAGE, AUDIO }