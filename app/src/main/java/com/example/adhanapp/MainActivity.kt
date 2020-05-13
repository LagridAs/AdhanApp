package com.example.adhanapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.azan.Azan
import com.azan.Method
import com.azan.astrologicalCalc.SimpleDate
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(),HoraireInterface {
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val PERMISSION_ID = 42
    var lat:Double?=null
    var long:Double?=null
    var brReceiver:BroadcastRec?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        brReceiver= BroadcastRec()
        brReceiver!!.scheduleJob(applicationContext)

        val intent=Intent(this,HoraireJobService::class.java)
        startService(intent)

        val loc = getLastLocation()
        val listSalat= getHoraireSalat(loc!!.latitude,loc!!.altitude)
        val td = SimpleDate(GregorianCalendar())
        dateAu.text = td.day.toString().plus("/").plus(td.month.toString()).plus(td.year.toString())
        Imsaak.text= listSalat[1].name.plus(" ").plus(listSalat[1].time)
        Fajr.text= listSalat[2].name.plus(" ").plus(listSalat[2].time)
        Sobh.text= listSalat[3].name.plus(" ").plus(listSalat[3].time)
        Dohr.text= listSalat[4].name.plus(" ").plus(listSalat[4].time)
        Asr.text= listSalat[5].name.plus(" ").plus(listSalat[5].time)
        Maghrib.text= listSalat[6].name.plus(" ").plus(listSalat[6].time)
        Ishaa.text= listSalat[7].name.plus(" ").plus(listSalat[7].time)

    }




     override fun getHoraireSalat(lat:Double, lon:Double):ArrayList<Horaire>{
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
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
    override fun getLastLocation(): Location? {
        var location:Location?= null
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    location = task.result!!
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        lat = location!!.latitude
                        long= location!!.longitude
                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
        return location
    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
    }

}
