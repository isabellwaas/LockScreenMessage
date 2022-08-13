package com.example.lockscreenmessage

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class LockUnlockBroadcastReceiver(val singleNotificationHelper: ISingleNotificationHelper, val keyguardManager: KeyguardManager): BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?)
    {
        if(context != null)
        {
            val persistentSaver:IPersistentSaver=PersistentSaver(context.getSharedPreferences("settings", Context.MODE_PRIVATE))
            if (intent?.getAction().equals(Intent.ACTION_SCREEN_OFF) && this.keyguardManager.isDeviceLocked)
            {
                this.singleNotificationHelper.sendNotificationIfNonePresent(context,context.getString(com.example.lockscreenmessage.R.string.lockScreenChannelId), persistentSaver.readValue(context.getString(com.example.lockscreenmessage.R.string.lockScreenMessageId), 11223344), persistentSaver.readValue(context.getString(com.example.lockscreenmessage.R.string.lockScreenMessageTitle), ""), persistentSaver.readValue(context.getString(com.example.lockscreenmessage.R.string.lockScreenMessageContent), ""))
            }
            else if((intent?.getAction().equals(Intent.ACTION_SCREEN_ON) && !this.keyguardManager.isDeviceLocked) || intent?.getAction().equals(Intent.ACTION_USER_PRESENT))
            {
                this.singleNotificationHelper.cancelNotificationIfOnePresent(context, persistentSaver.readValue(context.getString(com.example.lockscreenmessage.R.string.lockScreenMessageId), 11223344))
            }
        }
        else println("context is null")
    }

}