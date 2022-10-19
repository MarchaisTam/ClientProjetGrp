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
    fun hasAvailableStands(): Boolean {
        return availableStands >= 1
    }

    fun hasAvailableBikes(): Boolean {
        return availableBikes >= 1
    }

    fun toString(bikeStationBean: BikeStationBean): String {
        return bikeStationBean.toString()
    }


    fun getLocation() = Location("").apply {
        this.latitude = position.lat
        this.longitude = position.lon
    }
}