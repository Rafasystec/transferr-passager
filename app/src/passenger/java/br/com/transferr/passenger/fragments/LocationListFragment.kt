package br.com.transferr.passenger.fragments


//import br.com.transferr.passager.R.id.searchView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import br.com.transferr.fragments.SuperClassFragment
import br.com.transferr.R
import br.com.transferr.passenger.adapter.LocationAdapter
import br.com.transferr.passenger.extensions.defaultRecycleView
import br.com.transferr.passenger.extensions.setupToolbar
import br.com.transferr.passenger.extensions.switchFragmentToMainContent
import br.com.transferr.passenger.interfaces.OnResponseInterface
import br.com.transferr.passenger.model.Location
import br.com.transferr.passenger.webservices.WSLocation
import org.jetbrains.anko.progressDialog


/**
 * A simple [Fragment] subclass.
 * @author Rafael Rocha on 08/08/2018
 */
class LocationListFragment : SuperClassFragment() {

    private var recycleView : RecyclerView?=null
    private var locationList:List<Location>?=null
    private var locationAdapter: br.com.transferr.passenger.adapter.LocationAdapter?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.fragment_location_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setupToolbar(R.id.toolbar,context?.getString(R.string.localAndTours))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleView = defaultRecycleView(activity!!,R.id.rcLocationList)
        //initSearchView()
        requestAllLocations()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerViewAdapter()
    }

    fun setupRecyclerViewAdapter(){
        if(locationList != null){
            locationAdapter = br.com.transferr.passenger.adapter.LocationAdapter(locationList!!, { location -> onLocationClick(location) })
            recycleView?.adapter = locationAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_seacher,menu)
        var searchItem = menu?.findItem(R.id.menuSearch)
        var searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        //btnClickOnSearch.visibility = View.VISIBLE
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        //btnClickOnSearch.visibility = View.GONE
                        locationAdapter?.filter?.filter(newText)
                        return false
                    }

                }
        )


    }

    fun requestAllLocations(){
        var dialog = activity?.progressDialog(message = R.string.loading, title = R.string.wait)
        WSLocation.getAll(object : OnResponseInterface<List<Location>>{
            override fun onSuccess(body: List<Location>?) {
                dialog?.dismiss()
                locationList = body
                setupRecyclerViewAdapter()
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
        //var intent = Intent(context,LocationDetailActivity::class.java)
        //intent.putExtra(Location.LOCATION,location)
        //startActivity(intent)
        var fragment = TourOptionLisFragment()
        fragment.arguments = Bundle()
        fragment.arguments!!.putSerializable(Location.LOCATION,location)
        switchFragmentToMainContent(fragment)
    }
/*
    fun initSearchView(){
        searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        btnClickOnSearch.visibility = View.VISIBLE
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        btnClickOnSearch.visibility = View.GONE
                        locationAdapter?.filter?.filter(newText)
                        return false
                    }

                }
        )



    }
    */

}
