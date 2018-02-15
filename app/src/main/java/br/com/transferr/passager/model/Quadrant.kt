package br.com.transferr.passager.model

import com.google.android.gms.maps.model.LatLng

/**
 * Created by idoctor on 15/02/2018.
 */
class Quadrant {
    //For Far
    var farLeftLat  : Double = 0.toDouble()
    var farLeftLng  : Double = 0.toDouble()
    var farRightLat : Double = 0.toDouble()
    var farRightLng : Double = 0.toDouble()
    //For near
    var nearLeftLat : Double = 0.toDouble()
    var nearLeftLng : Double = 0.toDouble()
    var nearRightLat: Double = 0.toDouble()
    var nearRightLng: Double = 0.toDouble()
}