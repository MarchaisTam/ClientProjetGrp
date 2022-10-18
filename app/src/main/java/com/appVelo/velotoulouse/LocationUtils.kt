package com.appVelo.velotoulouse

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat

object LocationUtils {

    private fun getLastKnownLocation(c: Context): Location? {

        //Contrôle de la permission
        if (ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_DENIED
        ) {
            return null
        }

        var lm = c.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //on teste chaque provider(réseau, GPS...) et
        //on garde la localisation avec la meilleurs précision
        return lm.getProviders(true).map { lm.getLastKnownLocation(it) }
            .minByOrNull { it?.accuracy ?: Float.MAX_VALUE }
    }

    fun getLastKnownCoord(c :Context) : CoordBean? {

        var position: Location? = getLastKnownLocation(c) ?: return null
        return CoordBean(position!!.latitude, position!!.longitude)

    }

}