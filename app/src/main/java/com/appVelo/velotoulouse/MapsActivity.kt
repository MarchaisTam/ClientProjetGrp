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
import androidx.lifecycle.ViewModelProvider
import com.appVelo.velotoulouse.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.concurrent.thread


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
        binding.textView.isVisible = false
        setOnclickListeners()

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

        mMap.setInfoWindowAdapter(this)
        println("map ready")
        setModelObservers()
        println("model observé")


        // Add a marker in toulouse and move the camera
        val toulouse = LatLng(43.604652, 1.444209)
        mMap.addMarker(MarkerOptions().position(toulouse).title("Marker in Toulouse"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toulouse, 10f))

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
            //LocationUtils.getLastKnownCoord()
        } else {
            Toast.makeText(this, "Il faut la permission", Toast.LENGTH_SHORT).show()
        }
    }


    override fun getInfoContents(p0: Marker): View? {
        TODO("Not yet implemented")
    }

    override fun getInfoWindow(p0: Marker): View? {
        TODO("Not yet implemented")
    }


    fun askPermission() {
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
        model.data.observe(this) {
            it.forEach {
                val bikeStation = it.position.toLatLng()
                mMap.addMarker(
                    MarkerOptions().position(bikeStation).title(it.name).snippet(it.address)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike_station))
                )
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bikeStation, 12f))

                // val melbourneLatLng = LatLng(-37.81319, 144.96298)
                // val melbourne = mMap.addMarker(MarkerOptions().position(melbourneLatLng).title("Melbourne"))
                //melbourne?.showInfoWindow()
            }
        }

        model.errorMessage.observe(this) {
            if(it != null) {
                binding.textView.isVisible = true
                binding.textView.text = it
            }
            else {
                binding.textView.isVisible = false
            }
        }

    }

    fun setOnclickListeners() {
        binding.btGetBikeStations.setOnClickListener {
                model.loadData()
        }
    }
}