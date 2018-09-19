package br.com.transferr.passenger.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.com.transferr.R
import br.com.transferr.fragments.SuperClassFragment


/**
 * A simple [Fragment] subclass.
 */
class MainMenuFragment : SuperClassFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMainTitle(R.string.menu)
    }

}// Required empty public constructor
