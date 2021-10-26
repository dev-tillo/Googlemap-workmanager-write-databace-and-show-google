package com.example.googlemap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.googlemap.dao.Datahelper

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.googlemap.databinding.ActivityDbMapBinding
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

class DbMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDbMapBinding
    private lateinit var databaseHelper: Datahelper
    private lateinit var list: ArrayList<LatLng>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDbMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = Datahelper.getInstance(this)
        list = ArrayList()
        setLat()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setLat() {
        list.clear()
        val allMaps = databaseHelper.locationDao().getLocations()
        for (allMap in allMaps) {
            list.add(LatLng(allMap.lenga, allMap.longw))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .addAll(list)
        )
        val sydney = LatLng(list[0].latitude, list[0].longitude)
        mMap.addMarker(MarkerOptions().position(sydney).title("Your location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))
        mMap.setOnPolylineClickListener(this)
    }

    override fun onPolylineClick(p0: Polyline) {

    }
}