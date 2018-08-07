package br.com.transferr.passager.model

import br.com.transferr.model.Entity
import java.math.BigDecimal

/**
 * Created by Rafael Rocha on 07/08/2018.
 */
class TourOption : Entity() {

    private var name: String? = null
    private var description: String? = null
    private var value: BigDecimal? = null
    private var location: Location? = null
    private var images: List<String>? = null
}