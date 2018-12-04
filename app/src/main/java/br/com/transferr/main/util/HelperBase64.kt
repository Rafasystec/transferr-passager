package br.com.transferr.main.util

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Created by idoctor on 03/12/2018.
 */
object HelperBase64 {
    fun getPhotoString(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
    }
}