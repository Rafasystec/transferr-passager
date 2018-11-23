package br.com.transferr.passenger.model

import br.com.transferr.model.Driver
import br.com.transferr.passenger.model.enums.EnumTypeOfDriver
import java.util.*

/**
 * Created by Rafael Rocha on 10/08/2018.
 */
class PlainTour {

    var date: Date? = null
    var seatsRemaining: Int = 0
    var driver: Driver? = null
    var tourOption: TourOption? = null
    var open = java.lang.Boolean.TRUE

}