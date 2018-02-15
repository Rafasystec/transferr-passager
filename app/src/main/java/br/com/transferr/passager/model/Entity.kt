package br.com.transferr.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by idoctor on 08/02/2018.
 */
open class Entity() : Parcelable{
     var id: Long? = 1

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Entity> {
        override fun createFromParcel(parcel: Parcel): Entity {
            return Entity(parcel)
        }

        override fun newArray(size: Int): Array<Entity?> {
            return arrayOfNulls(size)
        }
    }
}