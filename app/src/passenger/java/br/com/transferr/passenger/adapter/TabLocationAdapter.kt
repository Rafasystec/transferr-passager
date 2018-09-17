package br.com.transferr.passenger.adapter


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import br.com.transferr.R
import br.com.transferr.passenger.fragments.DriverListFragment
import br.com.transferr.passenger.fragments.LocationDetailFragment
import br.com.transferr.passenger.fragments.PlainTourListFragment
import br.com.transferr.passenger.model.TourOption

/**
 * Created by Rafael Rocha on 10/08/2018.
 */
class TabLocationAdapter(private val context: Context, fm: FragmentManager,val tourOption:TourOption): FragmentPagerAdapter(fm) {
   
    override fun getItem(position: Int): Fragment {
       val fragment :Fragment = when (position) {
            0 -> DriverListFragment()
            1 -> PlainTourListFragment()
            else -> LocationDetailFragment()
        }

        with(fragment){
            arguments = Bundle()
            arguments!!.putSerializable(TourOption.TOUR_PARAMETER_KEY,tourOption)
        }

       //fragment.arguments = Bundle()
        //fragment.arguments.putSerializable(Location.LOCATION,local)

        return fragment
    }


    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? = when(position){
            0-> context.getString(R.string.drivers)
            1-> context.getString(R.string.plans)
            else -> context.getString(R.string.empty)
        }

}