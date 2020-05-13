package com.example.adhanapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

@SuppressLint("Registered")
class NotificationService: Service() {
    val CHANEL_ID="adhanApp.notification"
    val CHANNEL_SIREN_DESCRIPTION="Notification Adhan"

    override fun onBind(intent: Intent?): IBinder? {
            return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        addNotification(intent)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private fun addNotification(intent: Intent?) {
        val soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.adhan)

        val mChannel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = NotificationChannel(
                CHANEL_ID,
                CHANNEL_SIREN_DESCRIPTION,
                NotificationManager.IMPORTANCE_HIGH
            )
            mChannel.lightColor = Color.GRAY
            mChannel.enableLights(true)
            mChannel.description = CHANNEL_SIREN_DESCRIPTION
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            mChannel.setSound(soundUri, audioAttributes)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }



        val builder = NotificationCompat.Builder(this,CHANEL_ID)
            .setSmallIcon(android.R.drawable.alert_light_frame) //set icon for notification
            .setContentTitle("Notifications Example") //set title of notification
            .setContentText("This is a notification message") //this is notification message
            .setAutoCancel(true) // makes auto cancel of notification
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) //set priority of notification

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setSound(soundUri);
        }

        //val notificationIntent = Intent(this, NotificationActivity::class.java)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //notification message will get at NotificationActivity
        intent?.putExtra("message", "This is a notification message")
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(pendingIntent)

        // Add as notification
        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
    }
}