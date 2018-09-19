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
import br.com.transferr.passenger.adapter.PlainTourListAdapter
import br.com.transferr.passenger.application.ApplicationTransferr
import br.com.transferr.passenger.extensions.defaultRecycleView
import br.com.transferr.passenger.interfaces.OnResponseInterface
import br.com.transferr.passenger.model.PlainTour
import br.com.transferr.passenger.model.TourOption
import br.com.transferr.passenger.model.responses.ResponsePlainsByTourAndLocation
import br.com.transferr.passenger.webservices.WSPlainTour
import kotlinx.android.synthetic.passenger.layout_empty_list.*
import kotlinx.android.synthetic.passenger.layout_empty_list.view.*
import org.jetbrains.anko.progressDialog


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
        Log.i("INFO","Language device ${br.com.transferr.passenger.application.ApplicationTransferr.DEVICE_LANGUAGE}")
        if(llEmptyList != null) {
            llEmptyList.tvTextToAdd.text = "Sem Passeios no momento."
        }
    }

    override fun onResume() {
        super.onResume()
        loadRecyclesView(response)
        //loadPlainsByTourAndLocation()
    }

    private fun loadPlainsByTourAndLocation() {

        if(tourOption != null) {
            val dialog = showLoadingDialog()
            WSPlainTour.getByTourAndLocation(tourOption?.id!!, object : OnResponseInterface<ResponsePlainsByTourAndLocation> {
                override fun onSuccess(body: ResponsePlainsByTourAndLocation?) {
                    dialog?.dismiss()
                    response = body
                    loadRecyclesView(body!!)
                }

                override fun onError(message: String) {
                    dialog?.dismiss()
                    alertWarning(message)
                }

                override fun onFailure(t: Throwable?) {
                    dialog?.dismiss()
                    //alertErro(t?.message!!)

                }

            })
        }

    }

    private fun loadRecyclesView(responsePlainsByTourAndLocation: ResponsePlainsByTourAndLocation?){
        if(responsePlainsByTourAndLocation != null) {
            recycleViewFromTour?.adapter    = br.com.transferr.passenger.adapter.PlainTourListAdapter(responsePlainsByTourAndLocation.plainsFromTour!!, { plainTour: PlainTour -> onClickPlain(plainTour) })
            recycleViewFromLocation?.adapter = br.com.transferr.passenger.adapter.PlainTourListAdapter(responsePlainsByTourAndLocation.plainsFromLocation!!, { plainTour: PlainTour -> onClickPlain(plainTour) })
        }
    }

    /*fun checkIfIsEmpty(responseDrivers: ResponseDrivers){
        if(responseDrivers != null){
            val drivers = responseDrivers.drivers
            if(drivers?.isEmpty()!!){
                llEmptyList.visibility = View.VISIBLE
            }else{
                llEmptyList.visibility = View.GONE
            }
        }
    }*/

    fun onClickPlain(plainTour: PlainTour){

    }



}
