package br.com.transferr.passager.model

import br.com.transferr.model.Entity

/**
 * Created by Rafael Rocha on 27/07/2018.
 */
class SubCountry : Entity() {

    private val country: Country? = null
    private val name: String? = ""
    override fun toString(): String {
        return this.name!!
    }
}