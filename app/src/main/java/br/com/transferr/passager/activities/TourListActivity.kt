package br.com.transferr.passager.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import br.com.transferr.passager.R
import br.com.transferr.passager.adapter.LocationResponseAdapter
import br.com.transferr.passager.extensions.defaultRecycleView
import br.com.transferr.passager.interfaces.OnResponseInterface
import br.com.transferr.passager.model.Country
import br.com.transferr.passager.model.SubCountry
import br.com.transferr.passager.model.responses.ResponseLocation
import br.com.transferr.passager.webservices.WSCountry
import br.com.transferr.passager.webservices.WSLocation
import br.com.transferr.passager.webservices.WSSubCountry
import kotlinx.android.synthetic.main.activity_tour_list.*

class TourListActivity : SuperClassActivity() {

    private var recycleView : RecyclerView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_list)
        recycleView = defaultRecycleView(this,R.id.rcTourList)
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



    }

    override fun onResume() {
        super.onResume()
        loadSpCountry()
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
        WSLocation.doGetBySubCountry(subCountry.id!!, object : OnResponseInterface<List<ResponseLocation>>{
            override fun onSuccess(body: List<ResponseLocation>?) {
                recycleView!!.adapter = LocationResponseAdapter(body!!,
                        {responseLocation: ResponseLocation -> onLocationClick(responseLocation) })
            }

            override fun onError(message: String) {
                alertWarning(message)
            }

            override fun onFailure(t: Throwable?) {
                alertErro(t?.message!!)
            }

        })

    }
    fun onLocationClick(responseLocation: ResponseLocation){
        val intent = Intent(this,LocationActivity::class.java)
        intent.putExtra(ResponseLocation.LOCATION_PARAMETER_KEY,responseLocation.id)
        startActivity(intent)
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
}
