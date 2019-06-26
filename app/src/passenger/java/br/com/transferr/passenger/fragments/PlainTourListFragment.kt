package br.com.transferr.passenger.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.R
import br.com.transferr.extensions.defaultRecycleView
import br.com.transferr.extensions.showLoadingDialog
import br.com.transferr.fragments.SuperClassFragment
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.passenger.extensions.hasConnection
import br.com.transferr.passenger.extensions.hasInternetConnection
import br.com.transferr.passenger.model.PlainTour
import br.com.transferr.passenger.model.TourOption
import br.com.transferr.passenger.model.responses.ResponsePlainsByTourAndLocation
import br.com.transferr.passenger.webservices.WSPlainTour
import kotlinx.android.synthetic.main.layout_no_internet_connection.*
import kotlinx.android.synthetic.passenger.fragment_plain_tour_list.*
import kotlinx.android.synthetic.passenger.layout_empty_list.*
import kotlinx.android.synthetic.passenger.layout_empty_list.view.*
//import kotlinx.android.synthetic.passenger.layout_no_internet_connection.*


/**
 * A simple [Fragment] subclass.
 */
class PlainTourListFragment : SuperClassFragment() {

    private var recycleViewFromTour: RecyclerView?=null
    private var recycleViewFromLocation: RecyclerView?=null
   // private var location: Location?=null
    private var response:ResponsePlainsByTourAndLocation?=null
    private var tourOption:TourOption?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plain_tour_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tourOption              = arguments?.getSerializable(TourOption.TOUR_PARAMETER_KEY) as TourOption
        recycleViewFromTour     = defaultRecycleView(activity!!,R.id.rcPlainTourFromTour)
        recycleViewFromLocation = defaultRecycleView(activity!!,R.id.rcPlainTourFromLocation)
        loadPlainsByTourAndLocation()
        Log.i("INFO","Language device ${br.com.transferr.application.ApplicationTransferr.DEVICE_LANGUAGE}")
        if(llEmptyList != null) {
            llEmptyList.tvTextToAdd.text = getString(R.string.noPlainTourAtThisMoment)
            llEmptyList.tvTextDetail.text = getString(R.string.noPlainTourAtThisMomentDetail)
        }
        btnTryAgain.setOnClickListener {
            onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun loadPlainsByTourAndLocation() {

        if(tourOption != null) {
            val dialog = showLoadingDialog()
            WSPlainTour.getByTourAndLocation(tourOption?.id!!, object : OnResponseInterface<ResponsePlainsByTourAndLocation> {
                override fun onSuccess(body: ResponsePlainsByTourAndLocation?) {
                    dialog.dismiss()
                    response = body
                    loadRecyclesView(body!!)
                }

            },activity,dialog)
        }

    }

    private fun loadRecyclesView(responsePlainsByTourAndLocation: ResponsePlainsByTourAndLocation?){
        if(responsePlainsByTourAndLocation != null) {
            recycleViewFromTour?.adapter    = br.com.transferr.passenger.adapter.PlainTourListAdapter(responsePlainsByTourAndLocation.plainsFromTour!!, { plainTour: PlainTour -> onClickPlain(plainTour) })
            recycleViewFromLocation?.adapter = br.com.transferr.passenger.adapter.PlainTourListAdapter(responsePlainsByTourAndLocation.plainsFromLocation!!, { plainTour: PlainTour -> onClickPlain(plainTour) })
            if(responsePlainsByTourAndLocation.plainsFromLocation != null){
                if(responsePlainsByTourAndLocation.plainsFromLocation!!.isEmpty()){
                    btnSeeMorePlainsTour.visibility = View.GONE
                }else{
                    btnSeeMorePlainsTour.visibility = View.VISIBLE
                }
                checkIfIsEmpty(responsePlainsByTourAndLocation)
            }
        }else checkIfIsEmpty(responsePlainsByTourAndLocation)
    }

    private fun checkIfIsEmpty(plans: ResponsePlainsByTourAndLocation?){
        if(plans != null){
            val listFromTour      = plans.plainsFromTour
            val listFromLocation  = plans.plainsFromLocation
            if(listFromLocation?.isEmpty()!! && listFromTour?.isEmpty()!!){
                llEmptyList.visibility = View.VISIBLE
                btnSeeMorePlainsTour.visibility = View.GONE
            }else{
                llEmptyList.visibility = View.GONE
                btnSeeMorePlainsTour.visibility = View.VISIBLE
            }
        }else {
            llEmptyList.visibility = View.VISIBLE
            btnSeeMorePlainsTour.visibility = View.GONE
        }
    }

    private fun onClickPlain(plainTour: PlainTour){
        Log.d("PLAN", plainTour.toString())
    }

    private fun initView() {
        if (hasConnection(activity!!)) {
            if (hasInternetConnection()) {
                loadRecyclesView(response)
                showNoConnectionAdvice(false)
            } else {
                showNoConnectionAdvice(true)
            }
        } else {
            showNoConnectionAdvice(true)
        }
    }

    private fun showNoConnectionAdvice(visible:Boolean){
        if(visible) {

            llNoInternetConn.visibility     = View.VISIBLE
            btnTryAgain.visibility          = View.VISIBLE
            scViewPlanTour.visibility        = View.GONE
        }else{
            llNoInternetConn.visibility     = View.GONE
            btnTryAgain.visibility          = View.GONE
            scViewPlanTour.visibility       = View.VISIBLE

        }
    }



}
