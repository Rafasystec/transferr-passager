package br.com.transferr.passenger.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.R
import br.com.transferr.dialogs.CustomDIalogSendEmail
import br.com.transferr.extensions.defaultRecycleView
import br.com.transferr.extensions.showLoadingDialog
import br.com.transferr.fragments.SuperClassFragment
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.passenger.activities.LocationListActivity
import br.com.transferr.passenger.model.Location
import br.com.transferr.passenger.model.TourOption
import br.com.transferr.passenger.webservices.WSTourOption
import kotlinx.android.synthetic.passenger.fragment_tour_option_lis.*
import org.jetbrains.anko.progressDialog

/**
 * A simple [Fragment] subclass.
 */
class TourOptionLisFragment : SuperClassFragment() {


    var tourOptionList:List<TourOption>?=null

    private var recycleView : RecyclerView?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tour_option_lis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleView = defaultRecycleView(activity!!,R.id.rcTourList)
        var locationTmp = arguments?.getSerializable(Location.LOCATION)
        if(locationTmp != null){
            locationTmp = locationTmp as Location
            btnWhereToGo.text = locationTmp.name
            requestTourOptionByLocation(locationTmp)
        }else {
            requestTourOption()
        }
        btnWhereToGo.setOnClickListener{
            startActivity(Intent(activity,LocationListActivity::class.java))
        }
        setMainTitle(R.string.page_title_tour)

    }

    private fun requestTourOption(){
        var dialog = showLoadingDialog()
        WSTourOption.doGetAll(object : OnResponseInterface<List<TourOption>> {
            override fun onSuccess(body: List<TourOption>?) {
                //dialog?.dismiss()
                tourOptionList = body
                setTourOptionListAdapter()
                dialog?.dismiss()
            }
        },activity,dialog)
    }

    fun setTourOptionListAdapter(){
        if(tourOptionList != null && !tourOptionList!!.isEmpty()) {
            tvNoResultByLocation.visibility = View.GONE
            recycleView?.adapter = br.com.transferr.passenger.adapter.TourOptionAdapter(tourOptionList!!) { tourOption: TourOption -> onTourClick(tourOption) }
            showDialogSend()
        }else{
            tvNoResultByLocation.visibility = View.VISIBLE
        }
    }

    private fun onTourClick(tourOption: TourOption){
        startActivity(Intent(activity, br.com.transferr.passenger.activities.LocationActivity::class.java).putExtra(TourOption.TOUR_PARAMETER_KEY,tourOption))
    }

    private fun requestTourOptionByLocation(location:Location){
        val dialog = activity?.progressDialog(message = R.string.loading, title = R.string.wait)
        WSTourOption.getByLocation(location.id!!,object : OnResponseInterface<List<TourOption>>{
            override fun onSuccess(body: List<TourOption>?) {
                dialog?.dismiss()
                tourOptionList = body
                setTourOptionListAdapter()
                //showDialogSend()
            }
        },activity,dialog!!)
    }
    private fun showDialogSend(){
        CustomDIalogSendEmail.show(activity!!)
    }

}
