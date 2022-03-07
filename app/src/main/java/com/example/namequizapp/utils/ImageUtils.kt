package com.example.namequizapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*

object ImageUtils {

    /**
     * Function for converting a Bitmap to a Base64 encoded String
     */
    fun Bitmap.convertToString() : String {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG,100,stream)
        return Base64.getEncoder().encodeToString(stream.toByteArray())
    }

    /**
     * Function for converting a Base64 encoded String to a Bitmap
     */
    fun String.convertToBitmap() : Bitmap? {
        return try {
            val encodeByte = Base64.getDecoder().decode(this)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e : Exception) {
            e.message
            null
        }
    }
}