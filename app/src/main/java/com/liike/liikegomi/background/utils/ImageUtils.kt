package com.liike.liikegomi.background.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface

object ImageUtils {
    fun rotateImage(bitmap: Bitmap, absolutePath: String?): Bitmap {
        val matrix = when (absolutePath) {
            is String -> calculateRotationMatrix(absolutePath)
            else -> throw TypeCastException("fileData can only be a string path file and received null")
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun calculateRotationMatrix(path: String?): Matrix {
        val matrix = Matrix()
        try {
            val exifInterface = when (path) {
                is String -> ExifInterface(path)
                else -> throw TypeCastException("data can only be a string path file and received null")
            }
            when (exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return matrix
    }
}