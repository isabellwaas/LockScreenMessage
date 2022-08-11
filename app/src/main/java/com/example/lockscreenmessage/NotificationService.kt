package com.example.lockscreenmessage

import android.R
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.app.NotificationCompat


class NotificationService: Service()
{
    lateinit var receiver: BroadcastReceiver

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        val serviceNotificationHelper:NotificationHelper = NotificationHelper()
        serviceNotificationHelper.createNotificationChannel("serviceChannel", "Service message", "Shows that your service is running.", NotificationManager.IMPORTANCE_DEFAULT, Notification.VISIBILITY_PRIVATE, this.applicationContext)

        val notification: Notification = NotificationCompat.Builder(this, "serviceChannel")
            .setContentTitle("Example Service")
            .setContentText("Service is running")
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .build()

        startForeground(1, notification)
        return START_STICKY
    }

    override fun onCreate()
    {
        super.onCreate()
        val notificationHelper:NotificationHelper = NotificationHelper()
        notificationHelper.createNotificationChannel("lockScreenChannel", "Lock screen message", "The Lock screen message you have set.", NotificationManager.IMPORTANCE_HIGH, Notification.VISIBILITY_PUBLIC, this.applicationContext)

        receiver=LockUnlockBroadcastReceiver(notificationHelper, getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager)

        registerReceiver(receiver, IntentFilter().apply
        {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_USER_PRESENT)
        })
    }

    override fun onDestroy()
    {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

}