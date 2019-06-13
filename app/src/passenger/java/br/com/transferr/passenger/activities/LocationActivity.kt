package br.com.transferr.passenger.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.transferr.R
import br.com.transferr.main.util.LanguageDeviceUtil
import br.com.transferr.main.util.PicassoUtil
import br.com.transferr.passenger.extensions.hasConnection
import br.com.transferr.passenger.extensions.hasInternetConnection
import br.com.transferr.passenger.extensions.setupToolbar
import br.com.transferr.passenger.model.TourOption
import kotlinx.android.synthetic.passenger.activity_location.*
import kotlinx.android.synthetic.passenger.layout_location_details.*
import kotlinx.android.synthetic.passenger.layout_no_internet_connection.*


class LocationActivity : br.com.transferr.passenger.activities.SuperClassActivity() {

    var tourOption:TourOption?=null
    private var recyclerView: RecyclerView? = null
    private var imagens:ArrayList<String>?=null
    private var mAdapter: br.com.transferr.passenger.adapter.GalleryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        tourOption = intent.getSerializableExtra(TourOption.TOUR_PARAMETER_KEY) as TourOption
        setupToolbar(R.id.toolbarLocation,tourOption?.name,true)
        btnSeeDrivers.setOnClickListener {
            var intent = Intent(context, br.com.transferr.passenger.activities.LocationDetailActivity::class.java)
            intent.putExtra(TourOption.TOUR_PARAMETER_KEY,tourOption)
            startActivity(intent)
        }
        btnTryAgain.setOnClickListener {
            initView()
        }

    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        if (hasConnection(this)) {
            if (hasInternetConnection()) {
                loadFields()
            } else {
                showNoConnectionAdvice(true)
            }
        } else {
            showNoConnectionAdvice(true)
        }
    }

    private fun loadFields(){
        if(tourOption == null){
            return
        }
        showNoConnectionAdvice(false)
        PicassoUtil.build(tourOption?.profileUrl!!,ivMainPicture,null,true,true)
        tvLocationDescription.text = LanguageDeviceUtil.transform(tourOption!!.descriptionLanguage!!) //tourOption?.description
        val images: List<String> = tourOption!!.images!!
        initGallery(images)
    }



    private fun initGallery(images: List<String>){
        imagens = images as ArrayList<String>
        mAdapter = br.com.transferr.passenger.adapter.GalleryAdapter(this, imagens)
        var mLayoutManager = GridLayoutManager(this, 3)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = mLayoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = mAdapter
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

    private fun showNoConnectionAdvice(visible:Boolean){
        if(visible) {

            llNoInternetConn.visibility     = View.VISIBLE
            btnTryAgain.visibility          = View.VISIBLE
            btnSeeDrivers.visibility        = View.GONE
            scViewLocation.visibility       = View.GONE
        }else{
            llNoInternetConn.visibility     = View.GONE
            btnTryAgain.visibility          = View.GONE
            scViewLocation.visibility       = View.VISIBLE
            btnSeeDrivers.visibility        = View.VISIBLE
        }
    }


}
