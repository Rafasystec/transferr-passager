package br.com.transferr.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.*
import br.com.transferr.R
import br.com.transferr.activities.FrmPlainTourActivity
import br.com.transferr.activities.newlayout.DriverAddPlainTourActivity
import br.com.transferr.adapters.TourAdapter
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.showError
import br.com.transferr.extensions.showLoadingDialog
import br.com.transferr.extensions.showValidation
import br.com.transferr.model.PlainTour
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.main.util.Prefes
import br.com.transferr.webservices.PlainTourService
import kotlinx.android.synthetic.driver.fragment_driver_list_plain_tour.*


/**
 * A simple [Fragment] subclass.
 */
class DriverListPlainTourFragment : SuperClassFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_driver_list_plain_tour, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar,"Passeios",true)
        createListTour(emptyList())
        callWSToGetAllOpenDriverPlainTour()
        setHasOptionsMenu(true)
    }


    private fun createListTour(plainTours:List<PlainTour>){
        if(plainTours.isNotEmpty()) {
            tvListTourNoResult.visibility = View.GONE
            val recyclerView = rcviewToursFragment
            recyclerView.adapter = TourAdapter(plainTours, context!!, activity!!)
            val layoutManager = GridLayoutManager(context, GridLayoutManager.VERTICAL)
            recyclerView.layoutManager = layoutManager
        }else{
            tvListTourNoResult.visibility = View.VISIBLE
        }
    }

    private fun callWSToGetAllOpenDriverPlainTour(){
        var dialog = showLoadingDialog()
        PlainTourService.getDriverPlains(Prefes.driver.id!!,
                object : OnResponseInterface<List<PlainTour>> {
                    override fun onSuccess(body: List<PlainTour>?) {
                        try {
                            createListTour(body!!)
                        }finally {
                            dialog.dismiss()
                        }
                    }

                    override fun onError(message: String) {
                        try {
                            createListTour(emptyList())
                            showValidation(message)
                        }finally {
                            dialog.dismiss()
                        }
                    }

                    override fun onFailure(t: Throwable?) {
                       try{
                            createListTour(emptyList())
                            showError(t)
                       }finally {
                           dialog.dismiss()
                       }
                    }
                })
    }

    private fun startFrmPlainTourActivity(){
        startActivity(Intent(context, DriverAddPlainTourActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_list_plain_tour,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.menuAddNewPlan -> {
                startFrmPlainTourActivity()
                true
            }
            R.id.menuRefreshPlanList ->{
                callWSToGetAllOpenDriverPlainTour()
                rcviewToursFragment.adapter.notifyDataSetChanged()
                rcviewToursFragment.refreshDrawableState()
                true
            }else->{super.onOptionsItemSelected(item)}
        }

    }

}// Required empty public constructor
