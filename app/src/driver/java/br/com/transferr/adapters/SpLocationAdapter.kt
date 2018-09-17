package br.com.transferr.adapters


import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.com.transferr.R
import br.com.transferr.model.Location
import br.com.transferr.model.User



/**
 * Created by root on 03/04/18.
 */
class SpLocationAdapter(context: Context,values:List<Location>) : ArrayAdapter<Location>(context, R.layout.support_simple_spinner_dropdown_item,values) {

    var locations = values

    override fun getCount(): Int {
        return locations.size
    }

    override fun getItem(position: Int): Location? {
        return locations[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var label = super.getView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.text = locations[position].name
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var label = super.getDropDownView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.text = locations[position].name
        return label
    }
}