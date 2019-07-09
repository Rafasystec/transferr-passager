package br.com.transferr.activities.newlayout

import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.transferr.R
import br.com.transferr.activities.SuperClassActivity
import br.com.transferr.adapter.CovenantAdapter
import br.com.transferr.model.COVENANT_PARAM_NAME
import br.com.transferr.model.Covenant
import br.com.transferr.model.enums.EnumCategory
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.passenger.extensions.defaultRecycleView
import br.com.transferr.passenger.extensions.setupToolbar
import br.com.transferr.passenger.extensions.showLoadingDialog
import br.com.transferr.webservice.CovenantService
import kotlinx.android.synthetic.main.layout_content_covenant_list.*

class CovenantMenuActivity : SuperClassActivity() {

    var autoPartList:MutableList<Covenant>?= mutableListOf()
    var insuranceList:MutableList<Covenant>?=mutableListOf()
    var feedingList:MutableList<Covenant>?=mutableListOf()
    var workshopList:MutableList<Covenant>?=mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_covenant_menu)
        defaultRecycleView(rcAutoParts)
        defaultRecycleView(rcFeeding)
        defaultRecycleView(rcInsurance)
        defaultRecycleView(rcWorkshopList)
        setupToolbar(R.id.toolListCovenants,getString(R.string.covenants))
        getCovenants()
    }

    fun getCovenants(){
        var dialog = showLoadingDialog()
        CovenantService.getActives(object : OnResponseInterface<List<Covenant>>{
            override fun onSuccess(body: List<Covenant>?) {
                buildMenu(body!!)
                dialog.dismiss()
            }
        },this ,dialog)
    }

    private fun buildMenu(covenants:List<Covenant>){
        for(covenant in covenants){
            when(covenant.category){
                EnumCategory.AUTOPARTS -> {
                    autoPartList?.add(covenant)
                }
                EnumCategory.FOOD -> {
                    feedingList?.add(covenant)
                }
                EnumCategory.INSURANCE -> {
                    insuranceList?.add(covenant)
                }
                EnumCategory.WORKSHOP -> {
                    workshopList?.add(covenant)
                }
            }
        }
        if(autoPartList?.isNotEmpty()!!) {
            rcAutoParts.adapter = CovenantAdapter(autoPartList!!, this, this::onMenuListClick)
            llAutoParts.visibility = View.VISIBLE
        }else{
            llAutoParts.visibility = View.GONE
        }
        if(workshopList?.isNotEmpty()!!){
            rcWorkshopList.adapter = CovenantAdapter(workshopList!!,this,this::onMenuListClick)
            llWorkShops.visibility = View.VISIBLE
        }else{
            llWorkShops.visibility = View.GONE
        }
        if(feedingList?.isNotEmpty()!!){
            rcFeeding.adapter = CovenantAdapter(feedingList!!,this,this::onMenuListClick)
            llFeeding.visibility = View.VISIBLE
        }else{
            llFeeding.visibility = View.GONE
        }
        if(insuranceList?.isNotEmpty()!!){
            rcInsurance.adapter = CovenantAdapter(insuranceList!!,this,this::onMenuListClick)
            llInsurance.visibility = View.VISIBLE
        }else{
            llInsurance.visibility = View.GONE
        }
    }

    fun onMenuListClick(covenant: Covenant){
        val intent = Intent(this,CovenantDetailActivity::class.java)
        intent.putExtra(COVENANT_PARAM_NAME,covenant)
        startActivity(intent)

    }
}
