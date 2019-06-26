package br.com.transferr.passenger.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
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
import br.com.transferr.passenger.model.Location
import br.com.transferr.passenger.model.TourOption
import br.com.transferr.passenger.model.responses.ResponseDriver
import br.com.transferr.passenger.model.responses.ResponseDrivers
import br.com.transferr.passenger.webservices.WSDriver
import kotlinx.android.synthetic.main.layout_no_internet_connection.*
import kotlinx.android.synthetic.passenger.fragment_driver_list.*
import kotlinx.android.synthetic.passenger.layout_empty_list.*
import kotlinx.android.synthetic.passenger.layout_empty_list.view.*
//import kotlinx.android.synthetic.passenger.layout_no_internet_connection.*
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */
class DriverListFragment : SuperClassFragment() {

    private var recycleView: RecyclerView?=null
    //private var idLocation = 0
    private var location:Location?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        location = (arguments?.getSerializable(TourOption.TOUR_PARAMETER_KEY) as TourOption).location
        recycleView = defaultRecycleView(activity!!,R.id.rcDriversFromLocation)
        btnTryAgain.setOnClickListener {
            onResume()
        }
        if(llEmptyList != null) {
            llEmptyList.tvTextToAdd.text = getString(R.string.noDriverAtThisMoment)
            llEmptyList.tvTextDetail.text = ""
        }
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun loadDriversByLocation() {

        if(location != null) {
            val dialog = showLoadingDialog()
            WSDriver.doGetByLocation(location?.id!!, object : OnResponseInterface<ResponseDrivers> {
                override fun onSuccess(body: ResponseDrivers?) {
                    dialog.dismiss()
                    recycleView?.adapter = br.com.transferr.passenger.adapter.DriversResponseAdapter(body?.drivers!!, onClick = { driver: ResponseDriver -> cardViewOnClick(drivers = driver) })
                    checkIfIsEmpty(body)
                }
            },activity,dialog)
        }

    }

    private fun cardViewOnClick(drivers: ResponseDriver){
        activity?.toast("Selecionou o motorista ${drivers.name}")
    }

    fun checkIfIsEmpty(responseDrivers: ResponseDrivers){
        //if(responseDrivers != null){
            val drivers = responseDrivers.drivers
            if(drivers?.isEmpty()!!){
                llEmptyList.visibility = View.VISIBLE
            }else{
                llEmptyList.visibility = View.GONE
            }
        //}
    }

    private fun initView() {
        if (hasConnection(activity!!)) {
            if (hasInternetConnection()) {
                loadDriversByLocation()
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
            rcDriversFromLocation.visibility        = View.GONE

        }else{
            llNoInternetConn.visibility     = View.GONE
            btnTryAgain.visibility          = View.GONE
            rcDriversFromLocation.visibility       = View.VISIBLE

        }
    }

}
