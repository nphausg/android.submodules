/*
 * Created by nphau on 01/11/2021, 00:51
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package sg.nphau.android.shared.libs.file

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream

/**
 * Read Exif tags from the specified image input stream based on the stream type. Attribute mutation is not supported for input streams.
 * The given input stream will proceed from its current position.
 * Developers should close the input stream after use.
 * This constructor is not intended to be used with an input stream that performs any networking operations.
 * @see https://developer.android.com/reference/android/media/ExifInterface
 * */
object BitmapUtils {

    fun getOrientationAttribute(path: String?): Int {
        path ?: return ExifInterface.ORIENTATION_NORMAL
        val exif = ExifInterface(path)
        return exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
    }

    fun isNormalOrientation(path: String?): Boolean {
        return getOrientationAttribute(path) == ExifInterface.ORIENTATION_NORMAL
    }

    /**
     * Returns the transformation matrix if the orientation corresponds to one present in {@link
     * #INVERTED_EXIF_ORIENTATIONS}, else null.
     *
     * @param orientation the exif orientation
     * @return the transformation matrix if inverted orientation, else null.
     */
    private fun getTransformMatrix(orientation: Int): Matrix {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270F)
        }
        return matrix
    }

    /**
     *
     * */
    fun createRotatedFile(context: Context, path: String): File? {

        var rotatedTmpFile: File? = null

        try {
            // Create temporary rotated bitmap and store in memory
            val bitmap = BitmapFactory.decodeFile(path)
            val matrix = getTransformMatrix(getOrientationAttribute(path))
            val rotatedBitmap =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

            val cacheDir = File(context.cacheDir, "images")
            cacheDir.mkdirs()

            rotatedTmpFile = File.createTempFile("Tmp_", ".jpg", cacheDir)

            FileOutputStream(rotatedTmpFile).use { out ->
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rotatedTmpFile
    }

}