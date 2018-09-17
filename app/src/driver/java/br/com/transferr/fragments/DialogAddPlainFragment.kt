package br.com.transferr.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.com.transferr.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DialogAddPlainFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DialogAddPlainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DialogAddPlainFragment : DialogFragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_dialog_add_plain, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
