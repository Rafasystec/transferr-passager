package br.com.transferr.main.util

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import br.com.transferr.R
import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.webservices.SuperWebService
import okhttp3.OkHttpClient
import com.squareup.picasso.*

/**
 * Created by idoctor on 27/05/2019.
 */
object PicassoUtil {

    var picassoCache:LruCache?=null


    fun build(imageURI:String, view:ImageView,callback: Callback?=null, isFit:Boolean = false, isCenterCrop:Boolean = false, progressBar: ProgressBar? = null, context: Context?=null){

        var target : ImageViewTarget?=null
        if(progressBar != null) {
            target = ImageViewTarget(view, progressBar)
        }

        if(picassoCache == null){
            Log.d("DESTROY","Picasso cache is null")
            buildGlobalCache()
        }
        var pBuilder =
                if(context != null){
                    Picasso.Builder(context)
                }else{
                    Picasso.Builder(ApplicationTransferr.getInstance().applicationContext)
                }
                .memoryCache(picassoCache!!)
                .build()
                .load(imageURI)
                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .error(R.drawable.no_image)
        if(isFit){
            pBuilder.fit()
        }
        if(isCenterCrop){
            pBuilder.centerCrop()
        }
        if(callback == null) {
            if(target != null){
                pBuilder
                        //.placeholder(R.drawable.picasso_load_animation)
                        .into(target)
            }else{
                view.visibility = View.VISIBLE
                pBuilder
                        //.placeholder(R.drawable.picasso_load_animation)
                        .into(view)
            }
        }else{
            view.visibility = View.VISIBLE
            pBuilder
                    .placeholder(R.drawable.picasso_load_animation)
                    .into(view, callback)
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

    fun load(context: Context,url: String, imageView: ImageView,callback: Callback){
        Picasso.with(context)
                .load(url)
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .centerCrop()
                .into(imageView,callback)
    }

}