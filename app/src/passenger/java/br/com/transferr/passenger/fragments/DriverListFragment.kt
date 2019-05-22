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
import br.com.transferr.passenger.interfaces.OnResponseInterface
import br.com.transferr.passenger.model.Location
import br.com.transferr.passenger.model.TourOption
import br.com.transferr.passenger.model.responses.ResponseDriver
import br.com.transferr.passenger.model.responses.ResponseDrivers
import br.com.transferr.passenger.webservices.WSDriver
import kotlinx.android.synthetic.passenger.layout_empty_list.*
import kotlinx.android.synthetic.passenger.layout_empty_list.view.*
import org.jetbrains.anko.progressDialog
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
        if(llEmptyList != null) {
            llEmptyList.tvTextToAdd.text = getString(R.string.noDriverAtThisMoment)
            llEmptyList.tvTextDetail.text = ""
        }
    }

    override fun onResume() {
        super.onResume()
        loadDriversByLocation()
    }

    private fun loadDriversByLocation() {

        if(location != null) {
            val dialog = showLoadingDialog()
            WSDriver.doGetByLocation(location?.id!!, object : OnResponseInterface<ResponseDrivers> {
                override fun onSuccess(body: ResponseDrivers?) {
                    dialog?.dismiss()
                    recycleView?.adapter = br.com.transferr.passenger.adapter.DriversResponseAdapter(body?.drivers!!, onClick = { driver: ResponseDriver -> cardViewOnClick(drivers = driver) })
                    checkIfIsEmpty(body)
                }

                override fun onError(message: String) {
                    dialog?.dismiss()
                    alertWarning(message)
                }

                override fun onFailure(t: Throwable?) {
                    dialog?.dismiss()
                    alertErro(t?.message!!)

                }

            })
        }

    }

    private fun cardViewOnClick(drivers: ResponseDriver){
        activity?.toast("Selecionou o motorista ${drivers.name}")
    }

    fun checkIfIsEmpty(responseDrivers: ResponseDrivers){
        if(responseDrivers != null){
            val drivers = responseDrivers.drivers
            if(drivers?.isEmpty()!!){
                llEmptyList.visibility = View.VISIBLE
            }else{
                llEmptyList.visibility = View.GONE
            }
        }
    }

}
