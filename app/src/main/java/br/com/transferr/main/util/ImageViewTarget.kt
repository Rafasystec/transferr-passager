package br.com.transferr.main.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.ref.WeakReference


class ImageViewTarget(imageView:ImageView , progressBar:ProgressBar ) : Target{

    private val mImageViewReference: WeakReference<ImageView>? = WeakReference(imageView)
    private val mProgressBarReference: WeakReference<ProgressBar>? = WeakReference(progressBar)

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        val imageView = mImageViewReference?.get()
        imageView?.setImageDrawable(placeHolderDrawable)
        //imageView?.visibility = View.INVISIBLE
        val progressBar = mProgressBarReference?.get()
        if (progressBar != null) {
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun onBitmapFailed(errorDrawable: Drawable?) {
        mImageViewReference?.get()?.setImageDrawable(errorDrawable)

        val progressBar = mProgressBarReference?.get()
        if (progressBar != null) {
            progressBar.visibility = View.GONE
        }
        //mImageViewReference?.get()?.visibility = View.VISIBLE
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        //you can use this bitmap to load image in image view or save it in image file like the one in the above question.
        val imageView = mImageViewReference?.get()
        imageView?.setImageBitmap(bitmap)

        val progressBar = mProgressBarReference?.get()
        if (progressBar != null) {
            progressBar.visibility = View.GONE
        }
        mImageViewReference?.get()?.visibility = View.VISIBLE
    }


}