package br.com.transferr.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import br.com.transferr.R
import br.com.transferr.main.util.PicassoUtil
import br.com.transferr.model.Covenant
import kotlinx.android.synthetic.main.adapter_covenant_list_menu.view.*

class CovenantAdapter(private val covenants:List<Covenant>, private val context: Context, val callBack:(Covenant) -> Unit) : RecyclerView.Adapter<CovenantAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_covenant_list_menu, parent, false)
        val viewHolder = ViewHolder(view,context)
        viewHolder.itemView.setOnClickListener {
            val covenant = covenants[viewHolder.adapterPosition]
            callBack(covenant)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return covenants.size
    }

    override fun onBindViewHolder(view: ViewHolder, position: Int) {
        val covenant = covenants[position]
        //view.txtTitle.text = covenant.title
        view.txtDescription.text = covenant.description
        PicassoUtil.build(covenant.urlLogo!!,view.imgLogo)
    }

    class ViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView){
        //var txtTitle : TextView = itemView.txtConvItemTitle
        var txtDescription : TextView = itemView.txtConvItemDescription
        var imgLogo : ImageView = itemView.ivImgCovenantList
        //var progress: ProgressBar = itemView.progressCovList
    }
}