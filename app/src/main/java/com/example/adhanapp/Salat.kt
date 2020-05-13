package com.example.adhanapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.azan.Azan
import com.azan.Method
import com.azan.astrologicalCalc.SimpleDate
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class Salat {
    /*val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient

     fun getHoraireSalat(lat:Double, lon:Double):ArrayList<Horaire>{
        val  HoraireArrayList:ArrayList<Horaire> = arrayListOf()
        val today = SimpleDate(GregorianCalendar())
        val location = com.azan.astrologicalCalc.Location(lat,lon,1.0,0)
        val azan = Azan(location, Method.EGYPT_SURVEY)
        val prayerTimes = azan.getPrayerTimes(today)
        val imsaak = azan.getImsaak(today)

        HoraireArrayList.add(Horaire("Imsaak","$imsaak"))
        HoraireArrayList.add(Horaire("Fajr",prayerTimes.fajr().toString()))
        HoraireArrayList.add(Horaire("Sobh",prayerTimes.shuruq().toString()))
        HoraireArrayList.add(Horaire("Dohr",prayerTimes.thuhr().toString()))
        HoraireArrayList.add(Horaire("Asr",prayerTimes.assr().toString()))
        HoraireArrayList.add(Horaire("Maghrib",prayerTimes.maghrib().toString()))
        HoraireArrayList.add(Horaire("Ishaa",prayerTimes.ishaa().toString()))
        return HoraireArrayList
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }
    @SuppressLint("MissingPermission")
    override fun getLastLocation(){

        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this@MainActivity) { task ->
                    val location: Location? = task.result!!
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        getHoraireSalat(location.latitude,location.longitude)
                    }
                }
            } else {
                //Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }

    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            Log.d(ContentValues.TAG,"latitude :"+ mLastLocation?.latitude.toString() + " " + "longuitude"+ mLastLocation?.longitude.toString() )

        }
    }*/



}