package com.example.adhanapp

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.util.*


class HoraireJobService: JobService() {
    val interf= MainActivity() as HoraireInterface
    override fun onStopJob(params: JobParameters?): Boolean {
        val broadcastIntent= Intent()
        val lat = params!!.extras!!.getDouble("lat")
        val lon = params!!.extras!!.getDouble("lon")
        broadcastIntent.putExtra("lat",lat)
        broadcastIntent.putExtra("lon",lon)
        sendBroadcast(broadcastIntent)
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                Log.e("Null_TAG","Hello World")
                val lat = params!!.extras!!.getDouble("lat")
                val lon = params!!.extras!!.getDouble("lon")

                val salatList = interf.getHoraireSalat(lat, lon)
                val currentDateTime = LocalTime.now()
                for(item in salatList){
                    if ((LocalTime.parse(item.time))==currentDateTime){
                        val service = Intent(baseContext, NotificationService::class.java)
                        service.putExtra("salat",item)
                        startService(service)
                    }
                }

            }
        },60000,2)

        return true
    }



}