package com.appVelo.velotoulouse

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.thread

class MapsViewModel : ViewModel() {
    //Besoin de l'écran
    val data = MutableLiveData<List<BikeStationBean>>()
    val errorMessage = MutableLiveData("")
    val runInProgress = MutableLiveData(false)

    //Charger les données
    fun loadData() {
        //Rest des données
        //data.postValue(null)
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

}