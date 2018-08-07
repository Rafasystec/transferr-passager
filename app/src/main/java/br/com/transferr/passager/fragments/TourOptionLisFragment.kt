package br.com.transferr.passager.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.com.transferr.passager.R
import br.com.transferr.passager.adapter.TourOptionAdapter
import br.com.transferr.passager.extensions.defaultRecycleView
import br.com.transferr.passager.interfaces.OnResponseInterface
import br.com.transferr.passager.model.TourOption
import br.com.transferr.passager.webservices.WSTourOption
import kotlinx.android.synthetic.main.fragment_tour_option_lis.*
import org.jetbrains.anko.progressDialog


/**
 * A simple [Fragment] subclass.
 */
class TourOptionLisFragment : Fragment() {


    var tourOptionList:List<TourOption>?=null
    private var recycleView : RecyclerView?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        recycleView = defaultRecycleView(activity!!,R.id.rcTourList)
        requestTourOption()
        return inflater.inflate(R.layout.fragment_tour_option_lis, container, false)
    }

    fun requestTourOption(){
        WSTourOption.doGetAll(object : OnResponseInterface<List<TourOption>>{
            val dialog = activity?.progressDialog(message = R.string.loading, title = R.string.wait)
            override fun onSuccess(body: List<TourOption>?) {
                dialog?.dismiss()
                tourOptionList = body
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

    }

    fun setTourOptionListAdapter(){
        //rcTourList.adapter = TourOptionAdapter()
    }

}
