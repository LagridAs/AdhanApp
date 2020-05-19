package com.example.adhanapp

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer


class HoraireJobService: JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {
        /*********************envoie de Broadcast en cas d'arret du service par le systeme *********************/
        val RESTART_SERVICE = "restart.adhan"
        val i = Intent()
        i.action = RESTART_SERVICE
        i.setClass(this, AutoStart::class.java)
        this.sendBroadcast(i)
        return false
    }

    @SuppressLint("SimpleDateFormat", "CommitPrefEdits")
    override fun onStartJob(params: JobParameters?): Boolean {
        /*******************recuperation des horaires de salat*************************/
        val json = params!!.extras.getString("adhanTimes")
        Log.d(ContentValues.TAG, "json"+ json)

        val g = Gson()
        val adhanTimes: HoraireSalat = g.fromJson(json, HoraireSalat::class.java)
        Log.d(ContentValues.TAG, adhanTimes.toString())
        /*************enregistrer les horaires comme sharedPreferences****************/
        if (json != null) {
            saveAdhanTimes(json)
        }
        val sdf = SimpleDateFormat("HH:mm")
        var timeNow : String
        val context = this
        /**************envoie de notification lorsque l'heure de salat arrive******************/
        fixedRateTimer("timer",false,0,1000*30){
            timeNow = sdf.format(Date())
            Log.i("", timeNow)

            if (true){
                playNotification(context, "الصبح")
            }else if (timeNow == formatTime(adhanTimes.dohr)){
                playNotification(context, "الظهر")
            }else if (timeNow == formatTime(adhanTimes.asr)){
                playNotification(context, "العصر")
            }else if (timeNow == formatTime(adhanTimes.maghrib)){
                playNotification(context, "المغرب")
            }else if (timeNow == formatTime(adhanTimes.isha)){
                playNotification(context, "العشاء")
            }
        }

        return true
    }
    private fun playNotification(context: Context, adhan: String){
        var builder = NotificationCompat.Builder(context, "MyChannel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(adhan)
            .setContentText("حان الآن موعد أذان " + adhan)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(Uri.parse("android.resource://com.example.adhanapp/" + R.raw.adhan))

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify((Math.random()*1000).toInt(), builder.build())
        }
    }
    private fun formatTime(time: String): String{
        val timeArray = time.split(":")
        return timeArray[0]+":"+timeArray[1]
    }
    private fun saveAdhanTimes(adhan: String)
    {
        val sharedPreferences = getSharedPreferences(
            "adhanTimes",
            Context.MODE_PRIVATE
        )
        val myEdit = sharedPreferences.edit()
        myEdit.putString("adhanTimesPref",adhan)
        myEdit.apply()
    }


}