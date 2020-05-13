package com.example.adhanapp

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class BroadcastRec: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            scheduleJob(context)
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun scheduleJob(context: Context) {
        val  jobScheduler = context
                .getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(
            context,
            HoraireJobService::class.java
        )
        val jobInfo = JobInfo.Builder(
            1,
            componentName
        ) // setOverrideDeadline runs it immediately - you must have at least one constraint
            .setOverrideDeadline(0)
            .setMinimumLatency(1 * 1000)
            .setPersisted(true).build()
        jobScheduler.schedule(jobInfo)
    }

}