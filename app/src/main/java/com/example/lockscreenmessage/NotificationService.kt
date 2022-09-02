package com.example.lockscreenmessage

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder


class NotificationService: Service()
{
    lateinit var receiver: BroadcastReceiver

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        val serviceSingleNotificationHelper:ISingleNotificationHelper = SingleNotificationHelper()
        serviceSingleNotificationHelper.createNotificationChannel(getString(com.example.lockscreenmessage.R.string.service_channel_id), getString(com.example.lockscreenmessage.R.string.service_channel_name), getString(com.example.lockscreenmessage.R.string.service_channel_description), NotificationManager.IMPORTANCE_DEFAULT, Notification.VISIBILITY_PRIVATE, this.applicationContext)
        startForeground(44556677, serviceSingleNotificationHelper.buildNotificationWithPublicVersion(this, getString(com.example.lockscreenmessage.R.string.service_channel_id), 1, getString(com.example.lockscreenmessage.R.string.service_channel_name), getString(com.example.lockscreenmessage.R.string.service_message_content), NotificationManager.IMPORTANCE_DEFAULT, R.drawable.ic_notification))
        return START_STICKY
    }

    override fun onCreate()
    {
        super.onCreate()
        val lockScreenSingleNotificationHelper:ISingleNotificationHelper = SingleNotificationHelper()
        lockScreenSingleNotificationHelper.createNotificationChannel(getString(com.example.lockscreenmessage.R.string.lock_screen_channel_id), getString(com.example.lockscreenmessage.R.string.lock_screen_message_channel_name), getString(com.example.lockscreenmessage.R.string.lock_screen_message_channel_description), NotificationManager.IMPORTANCE_HIGH, Notification.VISIBILITY_PUBLIC, this.applicationContext)

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