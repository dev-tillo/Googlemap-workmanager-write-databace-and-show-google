package com.example.googlemap.activityes

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequestBuilder
import com.example.googlemap.DbMapActivity
import com.example.googlemap.WorkManager
import com.example.googlemap.dao.Datahelper
import com.example.googlemap.dao.LatLocation
import com.example.googlemap.databinding.ActivityMapDbhelperBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.net.InetAddress
import java.util.concurrent.TimeUnit

class MapDbhelper : AppCompatActivity() {

    lateinit var binding: ActivityMapDbhelperBinding
    private var locationPermissionGranted = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private lateinit var datahelper: Datahelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapDbhelperBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getLocationPermission()
        datahelper = Datahelper.getInstance(this)

//        datahelper.locationDao().addLocation(LatLocation(lenga = 41.326443, longw = 69.228549))

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding.apply {
            getDeviceLocation()
            if (locationPermissionGranted) {
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this@MapDbhelper, WorkManager::class.java)
                val pendingIntent = PendingIntent.getBroadcast(this@MapDbhelper, 1, intent, 0)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(),
                    15 * 60 * 1000,
                    pendingIntent)
            }
            cardMap.setOnClickListener {
                val intent = Intent(this@MapDbhelper, DbMapActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionGranted = false
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
        }
    }

    fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                }
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener {
                    if (it.isSuccessful) {
                        lastKnownLocation = it.result
                        if (lastKnownLocation != null) {
                            LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
                            binding.lengthTxt.text = lastKnownLocation?.latitude.toString()
                            binding.heightTxt.text = lastKnownLocation?.longitude.toString()
                        }
                    }
                }
            }
        } catch (e: Exception) {
        }
    }
}