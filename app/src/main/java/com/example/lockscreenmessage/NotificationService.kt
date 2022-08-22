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
        val serviceSingleNotificationHelper:ISingleNotificationHelper = SingleNotificationHelper()
        serviceSingleNotificationHelper.createNotificationChannel(getString(com.example.lockscreenmessage.R.string.service_channel_id), getString(com.example.lockscreenmessage.R.string.Service_channel_name), getString(com.example.lockscreenmessage.R.string.Service_channel_description), NotificationManager.IMPORTANCE_DEFAULT, Notification.VISIBILITY_PRIVATE, this.applicationContext)

        val notification: Notification = NotificationCompat.Builder(this, getString(com.example.lockscreenmessage.R.string.service_channel_id))
            .setContentTitle(getString(com.example.lockscreenmessage.R.string.Service_channel_name))
            .setContentText(getString(com.example.lockscreenmessage.R.string.Service_message_content))
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .build()

        startForeground(1, notification)
        return START_STICKY
    }

    override fun onCreate()
    {
        super.onCreate()
        val lockScreenSingleNotificationHelper:ISingleNotificationHelper = SingleNotificationHelper()
        lockScreenSingleNotificationHelper.createNotificationChannel(getString(com.example.lockscreenmessage.R.string.lock_screen_channel_id), getString(com.example.lockscreenmessage.R.string.Lock_screen_message_channel_name), getString(com.example.lockscreenmessage.R.string.Lock_screen_message_channel_description), NotificationManager.IMPORTANCE_HIGH, Notification.VISIBILITY_PUBLIC, this.applicationContext)

        receiver=LockUnlockBroadcastReceiver(lockScreenSingleNotificationHelper, getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager)

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