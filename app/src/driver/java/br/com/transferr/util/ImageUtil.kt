package br.com.transferr.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

/**
 * Created by root on 18/02/18.
 */
object ImageUtil {
    fun resize(file:File, width:Int,height:Int): Bitmap{
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath,options)
        options.inSampleSize = calculateInSampleSize(options,width,height)
        //Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return  BitmapFactory.decodeFile(file.absolutePath,options)
    }

    fun calculateInSampleSize(options:BitmapFactory.Options,width: Int,height: Int): Int{
        val heightOut = options.outHeight
        val widthOut  = options.outWidth
        var inSampleSize = 1
        if(heightOut > height || widthOut > width){
            val halfHeigh = heightOut / 2
            val halfWidth = widthOut / 2
            while(halfHeigh / inSampleSize >= height &&
                    halfWidth / inSampleSize >= width){
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

}