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
                val title:String=persistentSaver.readValue(context.getString(com.example.lockscreenmessage.R.string.lock_screen_message_title),"") ?: ""
                val text:String=persistentSaver.readValue(context.getString(com.example.lockscreenmessage.R.string.lock_screen_message_content), "") ?: ""
                if(!title.isBlank() || !text.isBlank()) this.singleNotificationHelper.sendNotificationIfNonePresent(context,context.getString(com.example.lockscreenmessage.R.string.lock_screen_channel_id), persistentSaver.readValue(context.getString(com.example.lockscreenmessage.R.string.lock_screen_message_id), 11223344), title, text)
            }
            else if((intent?.getAction().equals(Intent.ACTION_SCREEN_ON) && !this.keyguardManager.isDeviceLocked) || intent?.getAction().equals(Intent.ACTION_USER_PRESENT))
            {
                this.singleNotificationHelper.cancelNotificationIfOnePresent(context, persistentSaver.readValue(context.getString(com.example.lockscreenmessage.R.string.lock_screen_message_id), 11223344))
            }
        }
        else println("context is null")
    }

}