package com.appVelo.velotoulouse

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.appVelo.velotoulouse.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.concurrent.thread


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

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


        // Add a marker in Sydney and move the camera
        val toulouse = LatLng(43.604652, 1.444209)
        mMap.addMarker(MarkerOptions().position(toulouse).title("Marker in Toulouse"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(toulouse))

        binding.btGetBikeStations.isVisible = true
        binding.btGetNearestBikeStations.isVisible = true


    }

    override fun getInfoContents(p0: Marker): View? {
        TODO("Not yet implemented")
    }

    override fun getInfoWindow(p0: Marker): View? {
        TODO("Not yet implemented")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun askPermission() {

    }

    fun setModelObservers () {

    }

    fun setOnclickListeners () {
        binding.btGetBikeStations.setOnClickListener {

            thread {
                var res = RequestUtils.loadBikeStations()

                runOnUiThread {

                    res.forEach {
                        val bikeStation = LatLng(it.position.lat, it.position.lng)
                        mMap.addMarker(MarkerOptions().position(bikeStation).title(it.name).snippet(it.address))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bikeStation, 10f))


                       // val melbourneLatLng = LatLng(-37.81319, 144.96298)
                       // val melbourne = mMap.addMarker(MarkerOptions().position(melbourneLatLng).title("Melbourne")
                        //)
                        //melbourne?.showInfoWindow()



                    }



                }
            }

        }
    }
}