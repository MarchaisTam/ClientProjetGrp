package com.appVelo.velotoulouse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.thread

class MapsViewModel : ViewModel() {
    //Besoin de l'écran
    private val bikeStationData = MutableLiveData<List<BikeStationBean>>()
    val dataShown = MutableLiveData<List<BikeStationBean>>()
    val metroStationData = MutableLiveData<List<MetroStationBean>>()

    val errorMessage = MutableLiveData("")
    val runInProgress = MutableLiveData(false)

    //Charger les données
    private fun <T> load(data: MutableLiveData<T>, predicate: () -> T) {
        if (runInProgress.value!!) return

        errorMessage.postValue(null)
        runInProgress.postValue(true)

        thread {
            try {
                data.postValue(predicate.invoke())
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.postValue("Erreur : " + e.message)
            }
            runInProgress.postValue(false)
        }
    }

    fun loadBikeStationData() = load(bikeStationData) { RequestUtils.loadBikeStations() }
    fun loadMetroStationData() = load(metroStationData) { RequestUtils.loadMetroStation() }

    fun applyFilter(filter: BikeStationFilter) {
        if (!filter.isDataShown) return

        println("apply filter $filter")
        dataShown.postValue(
            bikeStationData.value?.filter {
                (!filter.byAvailableBikes || it.hasAvailableBikes())
                        && (!filter.byAvailableStands || it.hasAvailableStands())
            }?.sortedBy {
                filter.userLocation?.distanceTo(it.getLocation())
            }?.let {
                if (filter.nbShown is Int) it.take(filter.nbShown!!) else it
            }
        )
    }
}