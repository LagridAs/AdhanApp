package com.example.adhanapp

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import androidx.annotation.RequiresApi

class BroadcastRec: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val lat = intent!!.extras!!.getDouble("lat")
            val lon = intent!!.extras!!.getDouble("lon")
            val componentName =
                ComponentName(context, HoraireJobService::class.java)

            val bundle = PersistableBundle()
            bundle.putDouble("lat", lat)
            bundle.putDouble("lon", lon)

            val jobInfo = JobInfo.Builder(0, componentName)
                .setExtras(bundle)
                .setOverrideDeadline(0)
                .setMinimumLatency(1 * 1000)
                .setPersisted(true)
                .build()
            scheduleJob(context,jobInfo)
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun scheduleJob(context: Context,jobInfo:JobInfo) {
        val  jobScheduler = context
                .getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = jobScheduler.schedule(jobInfo)
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled!")
        } else {
            Log.d(TAG, "Job not scheduled")
        }
    }

}