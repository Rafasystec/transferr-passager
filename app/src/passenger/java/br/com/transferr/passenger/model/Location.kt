package br.com.transferr.passenger.model

import br.com.transferr.model.Entity
import br.com.transferr.model.enums.EnumLanguage

/**
 * Created by Rafael Rocha on 27/07/2018.
 */
class Location : Entity() {
    var subCountry: SubCountry? = null
    var name: String? = null
    var photoProfile: String? = null
    var images: List<String>? = null
    var description: String? = null
    var descriptionLanguage: Map<EnumLanguage, String>? = null

    companion object {
        val LOCATION = "location"
    }
}