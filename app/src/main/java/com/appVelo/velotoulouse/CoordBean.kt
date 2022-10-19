package com.appVelo.velotoulouse

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import kotlin.math.pow
import kotlin.math.sqrt

class CoordBean (
    var lat : Double,
    @SerializedName("lng", alternate = ["lon"])
    var lon : Double
        ) {
    fun toString(coordBean: CoordBean) : String {
        return coordBean.toString()
    }

    fun toLatLng() : LatLng {
        return LatLng(lat, lon)
    }
}