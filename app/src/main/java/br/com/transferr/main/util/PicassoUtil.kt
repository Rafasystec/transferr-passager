package br.com.transferr.main.util

import android.content.Context
import android.util.Log
import android.widget.ImageView
import br.com.transferr.R
import br.com.transferr.application.ApplicationTransferr
import com.squareup.picasso.*

/**
 * Created by idoctor on 27/05/2019.
 */
object PicassoUtil {

    var picassoCache:LruCache?=null

    fun build(imageURI:String, view:ImageView,callback: Callback?=null, isFit:Boolean = false, isCenterCrop:Boolean = false){
        //var picassoLocalCache = LruCache(ApplicationTransferr.getInstance().applicationContext)
        if(picassoCache == null){
            Log.d("DESTROY","Picasso cache is null")
        }
        var pBuilder = Picasso.Builder(ApplicationTransferr.getInstance().applicationContext)
                .memoryCache(picassoCache).build()
                .load(imageURI)
        if(isFit){
            pBuilder.fit()
        }
        if(isCenterCrop){
            pBuilder.centerCrop()
        }
        if(callback == null) {
            pBuilder.placeholder(R.drawable.no_photo_64).into(view)
        }else{
            pBuilder.placeholder(R.drawable.no_photo_64).into(view, callback)
        }


    }

    fun buildGlobalCache(){
        picassoCache = LruCache(ApplicationTransferr.getInstance().applicationContext)
    }

    fun clearGlobalCache(){
        if(picassoCache != null){
            picassoCache?.clear()
        }
    }
}