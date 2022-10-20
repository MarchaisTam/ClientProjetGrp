package com.appVelo.velotoulouse

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.appVelo.velotoulouse.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    val model by lazy { ViewModelProvider(this).get(MapsViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btGetBikeStations.isVisible = false
        binding.btGetNearestBikeStations.isVisible = false
        binding.tvError.isVisible = false
        binding.progressBar.isVisible = false
        setOnclickListeners()
        model.loadData()

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMaxZoomPreference(15f)
        //mMap.

        mMap.clear()

        askPermission()
        mMap.setInfoWindowAdapter(this)
        println("map ready")
        setModelObservers()
        println("model observé")

        // Move the camera
        val toulouse = LatLng(43.604652, 1.444209)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toulouse, 12f))

        binding.btGetBikeStations.isVisible = true
        binding.btGetNearestBikeStations.isVisible = true
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            //On a la permission
            onLocationPermissonGranted()
        } else {
            Toast.makeText(this, "Il faut la permission", Toast.LENGTH_SHORT).show()
        }
    }


    override fun getInfoContents(p0: Marker): View? {
        TODO("Not yet implemented")
    }

    override fun getInfoWindow(p0: Marker): View? {
        return null
    }


    fun askPermission() {
        println("dans askPermission")
        println(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION))
        //Est ce que j'ai la permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            //on a la permisssion

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0
            )
        }
    }

    fun setModelObservers() {
        model.dataShown.observe(this) {
            refreshMap()
        }

        model.errorMessage.observe(this) {
            if(!it.isNullOrEmpty()) {
                binding.tvError.isVisible = true
                binding.tvError.text = it
            }
            else {
                binding.tvError.isVisible = false
            }
        }

        model.runInProgress.observe(this) {
            binding.progressBar.isVisible = it != false
        }



    }

    fun setOnclickListeners() {
        binding.etNbShown.addTextChangedListener {
            binding.btGetNearestBikeStations.setText(binding.etNbShown.text.toString() + " stations à proximités")
        }

        binding.btGetBikeStations.setOnClickListener {
            model.applyFilter(binding.cbPlaceDispo.isChecked, binding.cbVeloDispo.isChecked)
        }

        binding.btGetNearestBikeStations.setOnClickListener {
            model.applyFilter(binding.cbPlaceDispo.isChecked, binding.cbVeloDispo.isChecked, LocationUtils.getLastKnownLocation(this), binding.etNbShown.text.toString().toInt())
        }

        binding.switch1.setOnClickListener {
            refreshMap()
        }
        
    }

    fun onLocationPermissonGranted () {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true

        }
    }

    fun displayBikeStations(latLngBounds : LatLngBounds.Builder) {
        onLocationPermissonGranted()


        model.dataShown.value?.forEach {
            val bikeStation = it.position.toLatLng()
            latLngBounds.include(bikeStation)
            mMap.addMarker(
                MarkerOptions().position(bikeStation)
                    //.title(it.name).snippet(it.address)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike_station)))?.setTag(bikeStation)
        }
    }

    fun displayMetroStations(latLngBounds : LatLngBounds.Builder) {
        if (!binding.switch1.isChecked) return

        model.metroStationData.value?.forEach {
            val metroStation = LatLng(it.latitude, it.longitude)
            latLngBounds.include(metroStation)
            mMap.addMarker(
                MarkerOptions().position(metroStation).title(it.name)
                    .icon(BitmapDescriptorFactory.fromResource(it.icon)))
        }
    }







    fun refreshMap() {
        mMap.clear()
        var latLngBounds = LatLngBounds.Builder()
        val userLocation = LocationUtils.getLastKnownCoord(this)
        if (userLocation != null) {
            latLngBounds.include(userLocation.toLatLng())
        } else {
            val toulouseTop = LatLng(43.605326, 1.441502)
            val toulouseDown = LatLng(43.602313, 1.444831)
            latLngBounds.include(toulouseDown)
            latLngBounds.include(toulouseTop)
        }

        displayBikeStations(latLngBounds)
        displayMetroStations(latLngBounds)
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 100))


    }

}