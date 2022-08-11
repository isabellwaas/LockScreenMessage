package com.example.lockscreenmessage

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class LockUnlockBroadcastReceiver(val notificationHelper: NotificationHelper, val keyguardManager: KeyguardManager): BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?)
    {
        if (intent?.getAction().equals(Intent.ACTION_SCREEN_OFF) && this.keyguardManager.isDeviceLocked)
        {
            println("screen off")
            if(context != null) this.notificationHelper.sendNotification(context, "lockScreenChannel", 123, "Contact Info", "This is Max's Phone. If you found it please call 012345679.")
            else println("context is null")
        }
        else if((intent?.getAction().equals(Intent.ACTION_SCREEN_ON) && !this.keyguardManager.isDeviceLocked) || intent?.getAction().equals(Intent.ACTION_USER_PRESENT))
        {
            println("screen on")
            if(context != null) this.notificationHelper.cancelNotification(context, 123)
            else println("context is null")
        }
    }

}