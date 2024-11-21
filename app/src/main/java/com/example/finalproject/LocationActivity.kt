package com.example.finalproject

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationActivity : AppCompatActivity(), OnMapReadyCallback  {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProvider: FusedLocationProviderClient

    private lateinit var locationEt: EditText
    private lateinit var t1: TextView
    private lateinit var t2: TextView

    var latitude: Double = 0.0
    var longitude: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_location)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@LocationActivity)

    }

    override fun onMapReady(p0: GoogleMap) {
        t1 = findViewById(R.id.text1)
        t2 = findViewById(R.id.text2)

        val btn = findViewById<Button>(R.id.but)

        btn.setOnClickListener {
            p0.clear()
            locationEt = findViewById(R.id.editTextText)
            googleMap = p0
            getLocationFromString(locationEt.text.toString())
            val latLng = LatLng(latitude, longitude)

            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(locationEt.text.toString())
            )

            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12f)
            googleMap.moveCamera(cameraUpdate)
        }

    }

    fun getLocationFromString(input: String) {
        val geocoder = Geocoder(this)
        val list: MutableList<Address>? = geocoder.getFromLocationName(input, 5)

        if (list.isNullOrEmpty()) {
            return
        }

        latitude = list[0].latitude
        longitude = list[0].longitude

        t1.text = "${latitude}"
        t2.text = "${longitude}"

    }


}