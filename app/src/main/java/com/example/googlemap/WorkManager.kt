package com.example.googlemap

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.googlemap.dao.Datahelper
import com.example.googlemap.dao.LatLocation
import com.google.android.gms.common.data.DataHolder
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class WorkManager : BroadcastReceiver() {

    private var locationPermission = true
    private lateinit var fusedProviderClient: FusedLocationProviderClient
    private var lastLocation: Location? = null
    private lateinit var datahelper: Datahelper

    override fun onReceive(p0: Context?, p1: Intent?) {
        fusedProviderClient = LocationServices.getFusedLocationProviderClient(p0)
        datahelper = Datahelper.getInstance(p0!!)
        getDeviceLocation(p0)
    }

    fun getDeviceLocation(context: Context) {

        try {
            if (locationPermission) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                }
                val locationResult = fusedProviderClient.lastLocation
                locationResult.addOnCompleteListener {
                    if (it.isSuccessful) {
                        lastLocation = it.result
                        if (lastLocation != null) {
                            LatLng(lastLocation!!.latitude, lastLocation!!.longitude)
                            datahelper.locationDao()
                                .addLocation(LatLocation(lenga = lastLocation?.latitude!!,
                                    longw = lastLocation?.longitude!!))
                        }
                    }

                }
            }
        } catch (e: Exception) {

        }
    }
}