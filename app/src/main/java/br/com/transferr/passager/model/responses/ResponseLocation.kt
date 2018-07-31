package br.com.transferr.passager.model.responses

/**
 * Created by Rafael Rocha on 26/07/18.
 */

class ResponseLocation {
    var id = 0L
    var name = ""
    var urlMainPicture = ""

    companion object {
        const val LOCATION_PARAMETER_KEY = "locationId"
    }

}