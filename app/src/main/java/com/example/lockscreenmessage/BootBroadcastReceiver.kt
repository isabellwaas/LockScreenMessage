package com.example.lockscreenmessage

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class BootBroadcastReceiver(): BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?)
    {
        if(context != null)
       {
            if(PersistentSaver(context.getSharedPreferences("settings", Context.MODE_PRIVATE)).readValue(context.getString(R.string.lock_screen_message_enabled), false))
            {
                ContextCompat.startForegroundService(context, Intent(context, NotificationService::class.java))
            }
       }
    }

}