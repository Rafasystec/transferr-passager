package br.com.transferr.passager.model

import br.com.transferr.model.Entity


/**
 * Created by Rafael Rocha on 27/07/2018.
 */
class Country : Entity() {
    var name: String? = null
    override fun toString(): String {
        return this.name!!
    }
}