package br.com.transferr.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.R
import br.com.transferr.adapters.TourAdapter
import br.com.transferr.model.Covenant
import br.com.transferr.model.PlainTour
import br.com.transferr.passenger.util.DateUtil
import kotlinx.android.synthetic.driver.rc_view_tours.view.*

class CovenantAdapter(private val tours:List<Covenant>, private val context: Context, val activity: Activity) : RecyclerView.Adapter<CovenantAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CovenantAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rc_view_tours, parent, false)
        return CovenantAdapter.ViewHolder(view, context)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(p0: CovenantAdapter.ViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class ViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView){


    }
}