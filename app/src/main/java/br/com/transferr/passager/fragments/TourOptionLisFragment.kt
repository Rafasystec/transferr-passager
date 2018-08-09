package br.com.transferr.passager.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.fragments.SuperClassFragment
import br.com.transferr.passager.R
import br.com.transferr.passager.activities.LocationActivity
import br.com.transferr.passager.adapter.TourOptionAdapter
import br.com.transferr.passager.extensions.defaultRecycleView
import br.com.transferr.passager.extensions.switchFragment
import br.com.transferr.passager.interfaces.OnResponseInterface
import br.com.transferr.passager.model.TourOption
import br.com.transferr.passager.webservices.WSTourOption
import kotlinx.android.synthetic.main.fragment_tour_option_lis.*
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
        requestTourOption()
        btnWhereToGo.setOnClickListener{
            //includeFragmentOnMainActivity(LocationListFragment())
            switchFragment(LocationListFragment())
        }
    }

    fun requestTourOption(){
        WSTourOption.doGetAll(object : OnResponseInterface<List<TourOption>>{
            val dialog = activity?.progressDialog(message = R.string.loading, title = R.string.wait)
            override fun onSuccess(body: List<TourOption>?) {
                dialog?.dismiss()
                tourOptionList = body
                setTourOptionListAdapter()
            }

            override fun onError(message: String) {
                dialog?.dismiss()
            }

            override fun onFailure(t: Throwable?) {
                dialog?.dismiss()
            }

        })
    }

    override fun onResume() {
        super.onResume()
        setTourOptionListAdapter()
    }

    fun setTourOptionListAdapter(){
        if(tourOptionList != null) {
            recycleView?.adapter = TourOptionAdapter(tourOptionList!!, { tourOption: TourOption -> onTourClick(tourOption) })
        }
    }

    private fun onTourClick(tourOption: TourOption){
        startActivity(Intent(activity,LocationActivity::class.java).putExtra(TourOption.TOUR_PARAMETER_KEY,tourOption))
    }

}
