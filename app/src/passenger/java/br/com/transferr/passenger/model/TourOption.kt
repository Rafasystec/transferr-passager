package br.com.transferr.passenger.model

import br.com.transferr.model.Entity
import br.com.transferr.model.enums.EnumLanguage
import java.math.BigDecimal




/**
 * Created by Rafael Rocha on 07/08/2018.
 */
class TourOption : Entity() {

    var name: String?           = null
    var description: String?    = null
    var value: BigDecimal?      = null
    var location: Location?     = null
    var images: List<String>?   = null
    var profileUrl: String?     = null
    var shortDescription: String? = null
    var descriptionLanguage: Map<EnumLanguage, String>? = null
    var shortDescriptionLanguage: Map<EnumLanguage, String>? = null
    companion object {
        const val TOUR_PARAMETER_KEY = "touroption"
        const val IMAGE_LIST_KEY     = "images"
    }
}