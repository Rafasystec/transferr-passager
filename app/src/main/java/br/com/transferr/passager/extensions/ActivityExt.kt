package br.com.transferr.passager.extensions

import android.app.Activity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by Rafael Rocha on 25/07/18.
 */

inline fun Activity.defaultRecycleView(view: Activity, resId:Int): RecyclerView {
    var recycleView = view.findViewById<RecyclerView>(resId)
    recycleView?.layoutManager = LinearLayoutManager(this)
    recycleView?.itemAnimator = DefaultItemAnimator()
    recycleView?.setHasFixedSize(true)
    return recycleView
}
