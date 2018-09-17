package br.com.transferr.adapters

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.com.transferr.R
import br.com.transferr.model.Location
import br.com.transferr.model.TourOption

/**
 * Created by root on 03/04/18.
 */
class SpTourOptionAdapter(context: Context, values:List<TourOption>) : ArrayAdapter<TourOption>(context, R.layout.spinner_layout,values) {

    var options = values

    override fun getCount(): Int {
        return options.size
    }

    override fun getItem(position: Int): TourOption? {
        return options[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var label = super.getView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.text = options[position].name
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var label = super.getDropDownView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.text = options[position].name
        return label
    }
}