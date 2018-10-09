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
    var namePassenger1: String?  = ""
    var telPassenger1: String?   = ""
    var notesPassenger1: String? = ""
    var namePassenger2: String?  = ""
    var telPassenger2: String?   = ""
    var notesPassenger2: String? = ""
    var namePassenger3: String?  = ""
    var telPassenger3: String?   = ""
    var notesPassenger3: String? = ""
    var notesOfPlain: String? = ""
}

