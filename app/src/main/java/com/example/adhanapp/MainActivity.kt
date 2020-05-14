package com.example.adhanapp

import android.Manifest
import android.app.job.JobInfo
import android.content.ComponentName
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.azan.Azan
import com.azan.Method
import com.azan.astrologicalCalc.SimpleDate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(),HoraireInterface {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val PERMISSION_CODE: Int = 1000
    var lat:Double?=null
    var long:Double?=null
    var brReceiver:BroadcastRec?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            Log.d(ContentValues.TAG,"fel on create")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            val permission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            requestPermissions(permission, PERMISSION_CODE)
        }else {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location ->
                    val loc = location
                    lat=loc.latitude
                    long=loc.altitude
                    Log.d(ContentValues.TAG,"latitude"+ lat)
                    val componentName =
                        ComponentName(this, HoraireJobService::class.java)

                    val bundle = PersistableBundle()
                    bundle.putDouble("lat", lat!!)
                    bundle.putDouble("lon", long!!)

                    val jobInfo = JobInfo.Builder(0, componentName)
                        .setExtras(bundle)
                        .setOverrideDeadline(0)
                        .setMinimumLatency(1 * 1000)
                        .setPersisted(true)
                        .build()
                    Log.d(ContentValues.TAG,"appel au brReceiver")

                    brReceiver= BroadcastRec()
                    brReceiver!!.scheduleJob(applicationContext,jobInfo)

                    val listSalat= getHoraireSalat(loc.latitude,loc.altitude)
                    val td = SimpleDate(GregorianCalendar())
                    dateAu.text = td.day.toString().plus("/").plus(td.month.toString()).plus(td.year.toString())
                    Imsaak.text= listSalat[1].name.plus(" ").plus(listSalat[1].time)
                    Fajr.text= listSalat[2].name.plus(" ").plus(listSalat[2].time)
                    Sobh.text= listSalat[3].name.plus(" ").plus(listSalat[3].time)
                    Dohr.text= listSalat[4].name.plus(" ").plus(listSalat[4].time)
                    Asr.text= listSalat[5].name.plus(" ").plus(listSalat[5].time)
                    Maghrib.text= listSalat[6].name.plus(" ").plus(listSalat[6].time)
                    Ishaa.text= listSalat[7].name.plus(" ").plus(listSalat[7].time)
                    Log.d(ContentValues.TAG,"liste remplit")

                }
        }


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

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    Toast.makeText(this, "Vous ne disposez pas des permissions necessaires", Toast.LENGTH_SHORT)
                }
                return
            }
        }
    }

}