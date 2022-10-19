package com.appVelo.velotoulouse

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.InputStreamReader

object RequestUtils {

    private val isServerUp = true
    private const val BIKESTATIONAPI_URL =
        "https://api.jcdecaux.com/vls/v1/stations?apiKey=2a1b07b2a523f81188fe34e348206a57ffa6f2a7&contract=Toulouse"
    private const val URL_SERVEUR = "http://2.4.228.11:8080"

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

    fun loadBikeStations() = this.let {
        val url = if (isServerUp) "$URL_SERVEUR/bike-stations" else "${BIKESTATIONAPI_URL}Toulouse"
        loadFromJson(url, Array<BikeStationBean>::class.java).toList()
    }
}