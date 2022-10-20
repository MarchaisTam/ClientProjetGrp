package com.appVelo.velotoulouse

import android.location.Location

class BikeStationBean(
    var name: String,
    var address: String,
    var position: CoordBean,
    var banking: Boolean,
    var stands: Int,
    var availableStands: Int,
    var availableBikes: Int,
    var status: String,
    var lastUpdate: Long,
    var id: Long
) {

    override fun toString(): String {
        return "$name\n$address"
    }

    fun hasAvailableStands() = availableStands > 0

    fun hasAvailableBikes() = availableBikes > 0

    fun getLocation() = Location("").apply {
        this.latitude = position.lat
        this.longitude = position.lon
    }
}