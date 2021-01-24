package com.example.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MainActivity : AppCompatActivity(), LocationListener {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if ((Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }

        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        getUserLocation()

    }
    @SuppressLint("MissingPermission")
    fun getUserLocation(){
        val locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0.1f,this)
    }

     override fun onLocationChanged(location: Location) {
         val mapv = findViewById<MapView>(R.id.map)
         mapv.setTileSource(TileSourceFactory.MAPNIK)
         mapv.setMultiTouchControls(true)
         mapv.controller.setZoom(9.0)
         Log.d("GEOLOCATION", "new latitude: ${location.latitude} and longitude: ${location.longitude}")
         val txt = findViewById<TextView>(R.id.coordinatesVIew)
         txt.text = getString(R.string.coordinates, location.latitude, location.longitude)
         getAddress(location.latitude, location.longitude)
         mapv.controller.setCenter(GeoPoint(location.latitude, location.longitude))


     }
      private fun getAddress(lat: Double?, lng: Double?) {
          val geocoder = Geocoder(this)
          val list = geocoder.getFromLocation(lat?:0.0, lng?:0.0, 1)
          Log.d("GEOLOCATION", list.toString())
          val adress = findViewById<TextView>(R.id.adressView)
          adress.text = list[0].getAddressLine(0)
          }

 }




