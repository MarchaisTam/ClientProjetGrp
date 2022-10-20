package com.appVelo.velotoulouse

import android.location.Location

class BikeStationFilter(
    var byAvailableBikes: Boolean = false,
    var byAvailableStands: Boolean = false,
    var userLocation: Location? = null,
    var nbShown: Int? = null,
    var isDataShown: Boolean = true
) {
    override fun toString(): String {
        return "$byAvailableBikes, $byAvailableStands, $userLocation, $nbShown, $isDataShown"
    }
}