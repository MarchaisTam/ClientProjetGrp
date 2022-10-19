package com.appVelo.velotoulouse

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

class CoordBean (
    var lat : Double,
    @SerializedName("lng", alternate = ["lon"])
    var lon : Double
)  {
    override fun toString() : String {
        return "lat= $lat , lon = $lon"
    }

    fun toLatLng() : LatLng {
        return LatLng(lat, lon)
    }
}