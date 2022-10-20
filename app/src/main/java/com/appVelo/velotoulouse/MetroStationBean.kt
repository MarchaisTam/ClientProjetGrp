package com.appVelo.velotoulouse

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MetroStationBean(
    var id : Int,
    var name : String,
    var longitude : Double,
    var latitude: Double,
    var line : String,
    var icon : Int
) {

    fun setIcon() {
        icon = when (line) {
            "A" -> R.drawable.ic_metro_station_red
            "B" -> R.drawable.ic_metro_station_yellow
            "C" -> R.drawable.ic_metro_station_blue
            else -> R.drawable.ic_metro_station_red
        }
    }



}

