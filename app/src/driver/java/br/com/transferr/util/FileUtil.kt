package br.com.transferr.util

import android.util.Base64
import java.io.File

/**
 * Created by root on 21/02/18.
 */
object FileUtil {

    fun toBase64(file:File):String{
        val bytes = file.readBytes()
        return Base64.encodeToString(bytes,Base64.NO_WRAP)
    }
}