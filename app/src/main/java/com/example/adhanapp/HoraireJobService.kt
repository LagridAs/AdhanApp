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
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                Log.e("NIlu_TAG","Hello World")
                val loc: Location? =interf.getLastLocation()
                val salatList = interf.getHoraireSalat(loc!!.altitude, loc.longitude)
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