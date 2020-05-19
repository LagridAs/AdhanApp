package com.example.adhanapp

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.content.ContentValues.TAG
import android.content.Context.MODE_APPEND
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson


class AutoStart: BroadcastReceiver() {
    val restartService = "restart.adhan"

    @SuppressLint("WrongConstant")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            Log.i("restarted", "phone restarted successfully")
            if (intent?.action.equals(restartService) || intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)){
                val componentName =
                    ComponentName(context, HoraireJobService::class.java)

                /********************recuperer les horaires shared preferences*********************/
                val sharedPreferences = context.getSharedPreferences(
                    "adhanTimesPref",
                    Context.MODE_APPEND
                )
                val jsonString = sharedPreferences.getString("adhanTimes","")
                /***************envoie des horairesau service pour le relancer********************/
                val bundle = PersistableBundle()
                bundle.putString("adhanTimes",jsonString)

                val jobInfo = JobInfo.Builder(0, componentName)
                    .setExtras(bundle)
                    .setOverrideDeadline(0)
                    .setMinimumLatency(1 * 1000)
                    .setPersisted(true)
                    .build()
                scheduleJob(context,jobInfo)
            }
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