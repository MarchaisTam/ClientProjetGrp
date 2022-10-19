package com.appVelo.velotoulouse

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.InputStreamReader
import kotlin.concurrent.thread

object RequestUtils {

    private const val BIKESTATIONAPI_URL =
        "https://api.jcdecaux.com/vls/v1/stations?apiKey=2a1b07b2a523f81188fe34e348206a57ffa6f2a7&contract=Toulouse"
    private const val URL_SERVEUR = "http://2.4.228.11:8080/bike-stations"
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
            loadFromJson("$URL_SERVEUR", Array<BikeStationBean>::class.java).toList()

}