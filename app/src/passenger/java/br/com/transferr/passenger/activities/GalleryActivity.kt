package br.com.transferr.passenger.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import br.com.transferr.R
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.transferr.passenger.adapter.GalleryAdapter
import br.com.transferr.passenger.model.TourOption
import br.com.transferr.passenger.fragments.dialogs.SlideshowDialogFragment




class GalleryActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var imagens:ArrayList<String>?=null
    private var mAdapter: br.com.transferr.passenger.adapter.GalleryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        //imagens = ArrayList()
        imagens = intent.getSerializableExtra(TourOption.IMAGE_LIST_KEY) as ArrayList<String>
        mAdapter = br.com.transferr.passenger.adapter.GalleryAdapter(this, imagens)
        var mLayoutManager = GridLayoutManager(this, 2)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = mLayoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = mAdapter
        //mAdapter?.notifyDataSetChanged()
        recyclerView?.addOnItemTouchListener(br.com.transferr.passenger.adapter.GalleryAdapter.RecyclerTouchListener(applicationContext, recyclerView, object : br.com.transferr.passenger.adapter.GalleryAdapter.ClickListener {
            override fun onClick(view: View, position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("images", imagens)
                bundle.putInt("position", position)

                val ft = supportFragmentManager.beginTransaction()
                val newFragment = br.com.transferr.passenger.fragments.dialogs.SlideshowDialogFragment()//.newInstance()
                newFragment.arguments = bundle
                newFragment.show(ft, "slideshow")
            }

            override fun onLongClick(view: View, position: Int) {

            }
        }))
    }
}
