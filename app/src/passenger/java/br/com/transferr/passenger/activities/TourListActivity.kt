package br.com.transferr.passenger.activities

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ArrayAdapter
import br.com.transferr.R
import br.com.transferr.passenger.adapter.LocationResponseAdapter
import br.com.transferr.passenger.extensions.defaultRecycleView
import br.com.transferr.passenger.interfaces.OnResponseInterface
import br.com.transferr.passenger.model.Country
import br.com.transferr.passenger.model.SubCountry
import br.com.transferr.passenger.model.responses.ResponseLocation
import br.com.transferr.passenger.webservices.WSCountry
import br.com.transferr.passenger.webservices.WSLocation
import br.com.transferr.passenger.webservices.WSSubCountry
import kotlinx.android.synthetic.passenger.activity_tour_list.*
import kotlinx.android.synthetic.passenger.layout_navigation_view.*
import kotlinx.android.synthetic.passenger.progress_bar_layout.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class TourListActivity : br.com.transferr.passenger.activities.SuperClassActivity() {

    private var recycleView : RecyclerView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_list)
        recycleView = defaultRecycleView(this,R.id.rcTourList)
        initNavigationBottomMenu()
        /*
        spCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var country:Country = parent?.getItemAtPosition(position) as Country
                loadSubCountryByCountry(country.id!!)
            }

        }

        spSubCountry.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var subCountry:SubCountry = parent?.getItemAtPosition(position) as SubCountry
                loadTourList(subCountry)
            }

        }
        */
    }

    override fun onResume() {
        super.onResume()
        //loadSpCountry()
    }

    private fun loadTourList(subCountry: SubCountry) {
        /*
        var location = ResponseLocation()
        with(location){
            name = "Lagoinha"
            urlMainPicture = "http://www.apousadadosol.com/images/lagoinha/lagoinha_23.jpg"
        }
        var location2 = ResponseLocation()
        with(location2){
            name = "Flecheiras"
            urlMainPicture = "https://segredosdomundo.r7.com/wp-content/uploads/2017/07/destaque-23-758x456.jpg"
        }
        */
        progress.visibility = View.VISIBLE
        WSLocation.doGetBySubCountry(subCountry.id!!, object : OnResponseInterface<List<ResponseLocation>>{
            override fun onSuccess(body: List<ResponseLocation>?) {
                recycleView!!.adapter = br.com.transferr.passenger.adapter.LocationResponseAdapter(body!!,
                        { responseLocation: ResponseLocation -> onLocationClick(responseLocation) })
                progress.visibility  = View.GONE
            }

            override fun onError(message: String) {
                alertWarning(message)
                progress.visibility  = View.GONE
            }

            override fun onFailure(t: Throwable?) {
                alertErro(t?.message!!)
                progress.visibility  = View.GONE
            }

        })

    }
    fun onLocationClick(responseLocation: ResponseLocation){
        //val intent = Intent(this,LocationActivity::class.java)
       // intent.putExtra(ResponseLocation.LOCATION_PARAMETER_KEY,responseLocation.id)
       // startActivity(intent)

        startActivity<br.com.transferr.passenger.activities.LocationActivity>(ResponseLocation.LOCATION_PARAMETER_KEY to responseLocation.id)
    }

    fun loadSpCountry(){
        WSCountry.doGetAll(
                object :OnResponseInterface<List<Country>>{
                    override fun onSuccess(body: List<Country>?) {
                        injectAdapterSpCountry(body!!)
                    }

                    override fun onError(message: String) {
                       alertWarning(message)
                    }

                    override fun onFailure(t: Throwable?) {
                        alertErro(t?.message!!)
                    }

                }
        )
    }

    fun loadSubCountryByCountry(idCountry:Long ){
        WSSubCountry.doGetAllByCountry(idCountry, object :OnResponseInterface<List<SubCountry>>{
            override fun onSuccess(body: List<SubCountry>?) {
                injectAdapterSpSubCountry(body!!)
            }

            override fun onError(message: String) {
                alertWarning(message)
            }

            override fun onFailure(t: Throwable?) {
                alertErro(t?.message!!)
            }

        })
    }

    fun injectAdapterSpCountry(list:List<Country>){
        spCountry.adapter = ArrayAdapter<Country>(this,R.layout.support_simple_spinner_dropdown_item,list)
    }

    fun injectAdapterSpSubCountry(list:List<SubCountry>){
        spSubCountry.adapter = ArrayAdapter<SubCountry>(this,R.layout.support_simple_spinner_dropdown_item,list)
    }

    fun initNavigationBottomMenu(){
        btnNavigationClient.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHomeCli -> {
                    toast("Inicio")
                    true
                }
                R.id.menuAgendaCli -> {
                    toast("Agenda")
                    true
                }
                R.id.menuHistoryCli -> {
                    toast("Histórico")
                    true
                }

                else ->{ true}
            }

        }
    }


}
