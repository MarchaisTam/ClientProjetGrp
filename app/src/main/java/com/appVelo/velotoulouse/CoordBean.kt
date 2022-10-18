package com.appVelo.velotoulouse

import com.google.android.gms.maps.model.LatLng

class CoordBean (
    var lat : Double,
    var lon : Double
        ) {
    fun toString(coordBean: CoordBean) : String {
        return coordBean.toString()
    }

    fun toLatLng() : LatLng {
        return LatLng(lat, lon)
    }
}