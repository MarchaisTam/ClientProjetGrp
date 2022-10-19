package com.appVelo.velotoulouse

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.thread

class MapsViewModel : ViewModel() {
    //Besoin de l'écran
    val data = MutableLiveData<List<BikeStationBean>>()
    val dataShown = MutableLiveData<List<BikeStationBean>>()
    val errorMessage = MutableLiveData("")
    val runInProgress = MutableLiveData(false)

    //Charger les données
    fun loadData() {
        if (runInProgress.value!!) return

        errorMessage.postValue(null)
        runInProgress.postValue(true)

        thread {
            try {
                data.postValue(RequestUtils.loadBikeStations())

            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.postValue("Erreur : " + e.message)
            }
            runInProgress.postValue(false)
        }
    }

    fun applyFilter(
        filterAvailableBikes: Boolean = false,
        filterAvailableStands: Boolean = false,
        userLocation: Location? = null,
        nbShown: Int? = null
    ) {
        dataShown.postValue(
            data.value?.filter {
                        (!filterAvailableBikes || it.hasAvailableBikes())
                        && (!filterAvailableStands || it.hasAvailableStands())
                    }?.sortedBy {
                        userLocation?.distanceTo(it.getLocation())
                    }?.let {
                        if (nbShown is Int) it.take (nbShown) else it
                    }
        )
    }

}