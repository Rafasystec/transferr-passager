package br.com.transferr.model

import java.util.*

/**
 * Created by root on 02/04/18.
 */
class PlainTour: Entity(){
    var date:Date?=null
    var seatsRemaining:Int=0
    var driver:Driver?=null
    var tourOption:TourOption?=null
    var open:Boolean?=null
}

