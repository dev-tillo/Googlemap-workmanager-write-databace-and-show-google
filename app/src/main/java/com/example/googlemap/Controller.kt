package com.example.googlemap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.googlemap.activityes.MapDbhelper
import com.example.googlemap.activityes.MapsActivity
import com.example.googlemap.databinding.ActivityControllerBinding

class Controller : AppCompatActivity() {

    lateinit var binding: ActivityControllerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.map.setOnClickListener {
            var intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        binding.maplocation.setOnClickListener {
            var intent = Intent(this, MapDbhelper::class.java)
            startActivity(intent)
        }
    }
}