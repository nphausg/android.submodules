/*
 * Created by nphau on 01/11/2021, 00:51
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package sg.nphau.android.shared.libs.file

import android.content.ContentResolver
import android.content.ContentUris
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import sg.nphau.android.shared.libs.timezone.timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sg.nphau.android.shared.data.entities.MediaFile
import sg.nphau.android.shared.data.entities.MediaType
import java.util.*
import java.util.concurrent.TimeUnit

object MediaStoreUtils {

    fun queryCursor(
        contentResolver: ContentResolver,
        idKey: String,
        contentUriKey: Uri,
        dateAddedKey: String,
        dateModifiedKey: String,
        displayNameKey: String,
        bucketKey: String
    ): Cursor? {

        /**
         * A key concept when working with Android [ContentProvider]s is something called
         * "projections". A projection is the list of columns to request from the provider,
         * and can be thought of (quite accurately) as the "SELECT ..." clause of a SQL
         * statement.
         *
         * It's not _required_ to provide a projection. In this case, one could pass `null`
         * in place of `projection` in the call to [ContentResolver.query], but requesting
         * more data than is required has a performance impact.
         *
         * For this sample, we only use a few columns of data, and so we'll request just a
         * subset of columns.
         */
        val projection = arrayOf(idKey, dateAddedKey, displayNameKey, bucketKey)

        /**
         * The `selection` is the "WHERE ..." clause of a SQL statement. It's also possible
         * to omit this by passing `null` in its place, and then all rows will be returned.
         * In this case we're using a selection based on the date the image was taken.
         *
         * Note that we've included a `?` in our selection. This stands in for a variable
         * which will be provided by the next variable.
         */
        val selection = "$dateAddedKey >= ?"

        /**
         * The `selectionArgs` is a list of values that will be filled in for each `?`
         * in the `selection`.
         */
        val selectionArgs = arrayOf(timestamp(day = 22, month = 10, year = 2008).toString())

        /**
         * Sort order to use. This can also be null, which will use the default sort order.
         */
        val sortOrder = "$dateModifiedKey DESC"

        return contentResolver.query(contentUriKey, projection, selection, selectionArgs, sortOrder)
    }

    private fun contentUri(contentUriKey: Uri, id: Long) =
        ContentUris.withAppendedId(contentUriKey, id)

    class Video {

        companion object {

            private const val ID = MediaStore.Video.Media._ID
            private const val DATE_ADDED = MediaStore.Video.Media.DATE_ADDED
            private const val DISPLAY_NAME = MediaStore.Video.Media.DISPLAY_NAME
            private const val DATE_MODIFIED = MediaStore.Video.Media.DATE_MODIFIED
            private const val BUCKET_DISPLAY_NAME = MediaStore.Video.Media.BUCKET_DISPLAY_NAME
            private val EXTERNAL_CONTENT_URI: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

            /**
             * Working with [ContentResolver]s can be slow, so we'll do this off the main
             * thread inside a coroutine.
             */
            suspend fun queryFiles(contentResolver: ContentResolver): List<MediaFile> {
                val videos = mutableListOf<MediaFile>()
                withContext(Dispatchers.IO) {
                    queryCursor(
                        idKey = ID,
                        contentResolver = contentResolver,
                        contentUriKey = EXTERNAL_CONTENT_URI,
                        dateAddedKey = DATE_ADDED,
                        dateModifiedKey = DATE_MODIFIED,
                        displayNameKey = DISPLAY_NAME,
                        bucketKey = BUCKET_DISPLAY_NAME
                    )?.use { cursor ->

                        /**
                         * In order to retrieve the data from the [Cursor] that's returned, we need to
                         * find which index matches each column that we're interested in.
                         *
                         * There are two ways to do this. The first is to use the method
                         * [Cursor.getColumnIndex] which returns -1 if the column ID isn't found. This
                         * is useful if the code is programmatically choosing which columns to request,
                         * but would like to use a single method to parse them into objects.
                         *
                         * In our case, since we know exactly which columns we'd like, and we know
                         * that they must be included (since they're all supported from API 1), we'll
                         * use [Cursor.getColumnIndexOrThrow]. This method will throw an
                         * [IllegalArgumentException] if the column named isn't found.
                         *
                         * In either case, while this method isn't slow, we'll want to cache the results
                         * to avoid having to look them up for each row.
                         */
                        val idCol = cursor.getIndex(ID)
                        val dateModifiedCol = cursor.getIndex(DATE_ADDED)
                        val displayNameCol = cursor.getIndex(DISPLAY_NAME)
                        val folderNameCol = cursor.getIndex(BUCKET_DISPLAY_NAME)

                        while (cursor.moveToNext()) {

                            // Here we'll use the column index that we found above.
                            val id = cursor.getLong(idCol)
                            val timeModified = cursor.getLong(dateModifiedCol)
                            val dateModified = Date(TimeUnit.SECONDS.toMillis(timeModified))
                            val displayName = cursor.getString(displayNameCol)
                            val folderName = cursor.getString(folderNameCol)

                            /**
                             * This is one of the trickiest parts:
                             *
                             * Since we're accessing images (using
                             * [MediaStore.Video.Media.EXTERNAL_CONTENT_URI], we'll use that
                             * as the base URI and append the ID of the image to it.
                             *
                             */
                            val contentUri = contentUri(EXTERNAL_CONTENT_URI, id)

                            videos += MediaFile(
                                id = id,
                                title = displayName,
                                contentUri = contentUri,
                                folderName = folderName,
                                description = dateModified.toString(),
                                mediaType = MediaType.VIDEO
                            )
                        }
                    }
                }
                return videos
            }
        }
    }

    class Image {

        companion object {
            private const val ID = MediaStore.Images.Media._ID
            private const val DATE_ADDED = MediaStore.Images.Media.DATE_ADDED
            private const val DISPLAY_NAME = MediaStore.Images.Media.DISPLAY_NAME
            private const val DATE_MODIFIED = MediaStore.Images.Media.DATE_MODIFIED
            private const val BUCKET_DISPLAY_NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
            private val EXTERNAL_CONTENT_URI: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            /**
             * Working with [ContentResolver]s can be slow, so we'll do this off the main
             * thread inside a coroutine.
             */
            suspend fun queryFiles(contentResolver: ContentResolver): List<MediaFile> {
                val videos = mutableListOf<MediaFile>()
                withContext(Dispatchers.IO) {
                    queryCursor(
                        idKey = ID,
                        contentResolver = contentResolver,
                        contentUriKey = EXTERNAL_CONTENT_URI,
                        dateAddedKey = DATE_ADDED,
                        dateModifiedKey = DATE_MODIFIED,
                        displayNameKey = DISPLAY_NAME,
                        bucketKey = BUCKET_DISPLAY_NAME
                    )?.use { cursor ->

                        /**
                         * In order to retrieve the data from the [Cursor] that's returned, we need to
                         * find which index matches each column that we're interested in.
                         *
                         * There are two ways to do this. The first is to use the method
                         * [Cursor.getColumnIndex] which returns -1 if the column ID isn't found. This
                         * is useful if the code is programmatically choosing which columns to request,
                         * but would like to use a single method to parse them into objects.
                         *
                         * In our case, since we know exactly which columns we'd like, and we know
                         * that they must be included (since they're all supported from API 1), we'll
                         * use [Cursor.getColumnIndexOrThrow]. This method will throw an
                         * [IllegalArgumentException] if the column named isn't found.
                         *
                         * In either case, while this method isn't slow, we'll want to cache the results
                         * to avoid having to look them up for each row.
                         */
                        val idCol = cursor.getIndex(ID)
                        val dateModifiedCol = cursor.getIndex(DATE_ADDED)
                        val displayNameCol = cursor.getIndex(DISPLAY_NAME)
                        val folderNameCol = cursor.getIndex(BUCKET_DISPLAY_NAME)

                        while (cursor.moveToNext()) {

                            // Here we'll use the column index that we found above.
                            val id = cursor.getLong(idCol)
                            val timeModified = cursor.getLong(dateModifiedCol)
                            val dateModified = Date(TimeUnit.SECONDS.toMillis(timeModified))
                            val displayName = cursor.getString(displayNameCol)
                            val folderName = cursor.getString(folderNameCol)

                            /**
                             * This is one of the trickiest parts:
                             *
                             * Since we're accessing images (using
                             * [MediaStore.Images.Media.EXTERNAL_CONTENT_URI], we'll use that
                             * as the base URI and append the ID of the image to it.
                             *
                             */
                            val contentUri = contentUri(EXTERNAL_CONTENT_URI, id)

                            videos += MediaFile(
                                id = id,
                                title = displayName,
                                contentUri = contentUri,
                                folderName = folderName,
                                description = dateModified.toString()
                            )
                        }
                    }
                }
                return videos
            }
        }
    }

    class Audio {

        companion object {
            private const val ID = MediaStore.Audio.Media._ID
            private const val DATE_ADDED = MediaStore.Audio.Media.DATE_ADDED
            private const val DISPLAY_NAME = MediaStore.Audio.Media.DISPLAY_NAME
            private const val DATE_MODIFIED = MediaStore.Audio.Media.DATE_MODIFIED
            private const val BUCKET_DISPLAY_NAME = MediaStore.Audio.Media.BUCKET_DISPLAY_NAME
            private val EXTERNAL_CONTENT_URI: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

            /**
             * Working with [ContentResolver]s can be slow, so we'll do this off the main
             * thread inside a coroutine.
             */
            suspend fun queryFiles(contentResolver: ContentResolver): List<MediaFile> {
                val audios = mutableListOf<MediaFile>()
                withContext(Dispatchers.IO) {
                    queryCursor(
                        idKey = ID,
                        contentResolver = contentResolver,
                        contentUriKey = EXTERNAL_CONTENT_URI,
                        dateAddedKey = DATE_ADDED,
                        dateModifiedKey = DATE_MODIFIED,
                        displayNameKey = DISPLAY_NAME,
                        bucketKey = BUCKET_DISPLAY_NAME
                    )?.use { cursor ->

                        /**
                         * In order to retrieve the data from the [Cursor] that's returned, we need to
                         * find which index matches each column that we're interested in.
                         *
                         * There are two ways to do this. The first is to use the method
                         * [Cursor.getColumnIndex] which returns -1 if the column ID isn't found. This
                         * is useful if the code is programmatically choosing which columns to request,
                         * but would like to use a single method to parse them into objects.
                         *
                         * In our case, since we know exactly which columns we'd like, and we know
                         * that they must be included (since they're all supported from API 1), we'll
                         * use [Cursor.getColumnIndexOrThrow]. This method will throw an
                         * [IllegalArgumentException] if the column named isn't found.
                         *
                         * In either case, while this method isn't slow, we'll want to cache the results
                         * to avoid having to look them up for each row.
                         */
                        val idCol = cursor.getIndex(ID)
                        val dateModifiedCol = cursor.getIndex(DATE_ADDED)
                        val displayNameCol = cursor.getIndex(DISPLAY_NAME)
                        val folderNameCol = cursor.getIndex(BUCKET_DISPLAY_NAME)

                        while (cursor.moveToNext()) {

                            // Here we'll use the column index that we found above.
                            val id = cursor.getLong(idCol)
                            val timeModified = cursor.getLong(dateModifiedCol)
                            val dateModified = Date(TimeUnit.SECONDS.toMillis(timeModified))
                            val displayName = cursor.getString(displayNameCol)
                            val folderName = cursor.getString(folderNameCol)

                            /**
                             * This is one of the trickiest parts:
                             *
                             * Since we're accessing images (using
                             * [MediaStore.Images.Media.EXTERNAL_CONTENT_URI], we'll use that
                             * as the base URI and append the ID of the image to it.
                             *
                             */
                            val contentUri = contentUri(EXTERNAL_CONTENT_URI, id)

                            audios += MediaFile(
                                id = id,
                                title = displayName,
                                contentUri = contentUri,
                                folderName = folderName,
                                mediaType = MediaType.AUDIO,
                                description = dateModified.toString()
                            )
                        }
                    }
                }
                return audios
            }
        }
    }
}

fun List<MediaFile>.groupByFolder() = groupBy { it.folderName }
    .entries
    .map { entry ->
        MediaFile(
            files = entry.value,
            folderName = entry.key
        ).apply {
            entry.value.firstOrNull()?.let {
                this.id = it.id
                this.contentUri = it.contentUri
                this.title = it.title
                this.description = it.description
            }
        }
    }

fun Cursor.getIndex(columnName: String) = getColumnIndexOrThrow(columnName)

/**
 * Convenience extension method to register a [ContentObserver] given a lambda.
 */
fun ContentResolver.registerObserver(
    uri: Uri, observer: (selfChange: Boolean) -> Unit
): ContentObserver {
    val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    registerContentObserver(uri, true, contentObserver)
    return contentObserver
}