package br.com.transferr.passenger.fragments


//import br.com.transferr.passager.R.id.searchView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import br.com.transferr.R
import br.com.transferr.extensions.defaultRecycleView
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.showLoadingDialog
import br.com.transferr.extensions.switchFragmentToMainContent
import br.com.transferr.fragments.SuperClassFragment
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.passenger.model.Location
import br.com.transferr.passenger.webservices.WSLocation


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
        setupToolbar(R.id.toolbarLocationList,context?.getString(R.string.places))
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
        val searchEditText = searchView.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text) as EditText
        searchEditText.setTextColor(resources.getColor(android.R.color.black))
        searchEditText.setHintTextColor(resources.getColor(android.R.color.black))
        val searchClose = searchView.findViewById<ImageView>(android.support.v7.appcompat.R.id.search_close_btn)
        searchClose.setImageResource(R.drawable.arrow_left)


    }

    fun requestAllLocations(){
        var dialog = showLoadingDialog()
        WSLocation.getAll(object : OnResponseInterface<List<Location>> {
            override fun onSuccess(body: List<Location>?) {
                dialog?.dismiss()
                locationList = body
                setupRecyclerViewAdapter()
            }
        },activity,dialog)
    }

    fun onLocationClick(location: Location){
        var fragment = TourOptionLisFragment()
        fragment.arguments = Bundle()
        fragment.arguments!!.putSerializable(Location.LOCATION,location)
        switchFragmentToMainContent(fragment)
    }

}
