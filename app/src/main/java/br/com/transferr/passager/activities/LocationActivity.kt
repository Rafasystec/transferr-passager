package br.com.transferr.passager.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.transferr.passager.R
import br.com.transferr.passager.adapter.GalleryAdapter
import br.com.transferr.passager.fragments.dialogs.SlideshowDialogFragment
import br.com.transferr.passager.model.TourOption
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_location_details.*
import org.jetbrains.anko.startActivity


class LocationActivity : SuperClassActivity() {

    //var idLocation:Long?=null
    var tourOption:TourOption?=null
    private var recyclerView: RecyclerView? = null
    private var imagens:ArrayList<String>?=null
    private var mAdapter: GalleryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        tourOption = intent.getSerializableExtra(TourOption.TOUR_PARAMETER_KEY) as TourOption
        btnSeeDrivers.setOnClickListener {
            var intent = Intent(context,LocationDetailActivity::class.java)
            //intent.putExtra(Location.LOCATION,tourOption?.location)
            intent.putExtra(TourOption.TOUR_PARAMETER_KEY,tourOption)
            startActivity(intent)

            //var intent = Intent(this,DriverListActivity::class.java)
            //intent.putExtra(ResponseLocation.LOCATION_PARAMETER_KEY,idLocation!!)
            //startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        //loadLocation()
        loadFields()
    }

    private fun loadFields(){
        if(tourOption == null){
            return
        }
        Picasso.with(this).load(tourOption?.profileUrl)
                .memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE)
                .fit()
                .centerCrop()
                .into(ivMainPicture)
        tvLocationTitle.text = tourOption?.name
        tvLocationDescription.text = tourOption?.description
        /*
        var i = 0
        tourOption?.images?.forEach {
            ++i
            when (i) {
                1 -> Picasso.with(this).load(it).into(ivPictureOne)
                2 -> Picasso.with(this).load(it).into(ivPictureTwo)
                3 -> Picasso.with(this).load(it).into(ivPictureThree)
                4 -> Picasso.with(this).load(it).into(ivPictureFour)
                5 -> Picasso.with(this).load(it).into(ivPictureFive)
                6 -> Picasso.with(this).load(it).into(ivPictureSix)
            }
        }
        val images: List<String> = tourOption!!.images!!
        ivPictureOne.setOnClickListener {
            callGalleryActivity(images)
        }
        ivPictureTwo.setOnClickListener {
            callGalleryActivity(images)
        }
        ivPictureThree.setOnClickListener {
            callGalleryActivity(images)
        }
        ivPictureFour.setOnClickListener {
            callGalleryActivity(images)
        }
        ivPictureFive.setOnClickListener {
            callGalleryActivity(images)
        }
        ivPictureSix.setOnClickListener {
            callGalleryActivity(images)
        }
        */
        val images: List<String> = tourOption!!.images!!
        initGallery(images)
    }

    private fun callGalleryActivity(images: List<String>){
        startActivity<GalleryActivity>(TourOption.IMAGE_LIST_KEY to images)
    }

    private fun initGallery(images: List<String>){
        //imagens = intent.getSerializableExtra(TourOption.IMAGE_LIST_KEY) as ArrayList<String>
        imagens = images as ArrayList<String>
        mAdapter = GalleryAdapter(this,imagens)
        var mLayoutManager = GridLayoutManager(this, 3)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = mLayoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = mAdapter
        //mAdapter?.notifyDataSetChanged()
        recyclerView?.addOnItemTouchListener(GalleryAdapter.RecyclerTouchListener(applicationContext, recyclerView, object : GalleryAdapter.ClickListener {
            override fun onClick(view: View, position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("images", imagens)
                bundle.putInt("position", position)

                val ft = supportFragmentManager.beginTransaction()
                val newFragment = SlideshowDialogFragment()//.newInstance()
                newFragment.arguments = bundle
                newFragment.show(ft, "slideshow")
            }

            override fun onLongClick(view: View, position: Int) {

            }
        }))
    }
}
