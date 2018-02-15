package br.com.transferr.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.passager.R

import br.com.transferr.util.PermissionUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/**
 * A simple [Fragment] subclass.
 */
class MapsFragment : SuperClassFragment(), OnMapReadyCallback {

    private var map : GoogleMap? = null

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        this.map = map
        //Cria o objeto longitude e latitude
        val location = LatLng(-3.7318616,-38.5266704)
        //Posiciona o mapa na cordenada (zoom 13)
        val update = CameraUpdateFactory.newLatLngZoom(location,13f)
        map.moveCamera(update)
        //Marcar olocal no mapa
        map.addMarker(MarkerOptions()
                .title("Marcado aqui")
                .snippet("Alguma decricao")
                .position(location)
        )
        //Tipo do mapa
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        val allowed = PermissionUtil.validate(activity,1,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        if(allowed) {
            map.isMyLocationEnabled = true
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater?.inflate(R.layout.fragment_maps, container, false)
        //Inicia o map
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }


}// Required empty public constructor
