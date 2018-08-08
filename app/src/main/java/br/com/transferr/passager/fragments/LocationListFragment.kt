package br.com.transferr.passager.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.fragments.SuperClassFragment

import br.com.transferr.passager.R
import br.com.transferr.passager.adapter.LocationAdapter
import br.com.transferr.passager.extensions.defaultRecycleView
import br.com.transferr.passager.interfaces.OnResponseInterface
import br.com.transferr.passager.model.Location
import br.com.transferr.passager.webservices.WSLocation
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 * @author Rafael Rocha on 08/08/2018
 */
class LocationListFragment : SuperClassFragment() {

    private var recycleView : RecyclerView?=null
    private var locationList:List<Location>?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.actionBar?.title = "Locais"
        return inflater.inflate(R.layout.fragment_location_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleView = defaultRecycleView(activity!!,R.id.rcLocationList)
        requestAllLocations()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerViewAdapter()
    }

    fun setupRecyclerViewAdapter(){
        if(locationList != null){
            recycleView?.adapter = LocationAdapter(locationList!!,{location -> onLocationClick(location) })
        }
    }

    fun requestAllLocations(){
        dialog?.show()
        WSLocation.getAll(object : OnResponseInterface<List<Location>>{
            override fun onSuccess(body: List<Location>?) {
                dialog?.dismiss()
                locationList = body
            }

            override fun onError(message: String) {
                dialog?.dismiss()
            }

            override fun onFailure(t: Throwable?) {
                dialog?.dismiss()
            }

        })
    }

    fun onLocationClick(location: Location){
        activity?.toast("Clicou em ${location.name}")
    }

}
