package com.appVelo.velotoulouse

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.InputStreamReader

object RequestUtils {

    private const val URL_SERVEUR = "http://2.4.228.11:8080"
    private const val URL_METRO_API = "http://90.76.49.229:8080/getAllStations"

    private val client = OkHttpClient()
    private val gson = Gson()

    private fun sendGet(url: String): Response {
        println("request url=$url")
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw Exception("Réponse du serveur incorrecte : ${response.code} - ${response.message}")
        }
        return response
    }

    private fun <T> loadFromJson(url: String, t: Class<T>): T =
        InputStreamReader(sendGet(url).body!!.byteStream()).use {
            gson.fromJson(it, t)
        }

    fun loadBikeStations() =
        loadFromJson("$URL_SERVEUR/bike-stations", Array<BikeStationBean>::class.java).toList()

    fun loadMetroStation() =
        loadFromJson(URL_METRO_API, Array<MetroStationBean>::class.java).map {
            it.setIcon()
            it
        }
}