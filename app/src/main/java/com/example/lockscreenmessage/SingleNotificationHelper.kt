package com.example.lockscreenmessage

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class SingleNotificationHelper(override var notificationIsPresent: Boolean = false):ISingleNotificationHelper
{
    override fun createNotificationChannel(id:String, name:String, description:String, importance:Int, lockscreenVisibility:Int, context:Context):Unit
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val notificationChannel = NotificationChannel(id, name, importance)
            notificationChannel.setDescription(description)
            notificationChannel.lockscreenVisibility=lockscreenVisibility

            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    override fun sendNotificationIfNonePresent(context:Context, channelId:String, notificationId:Int, title:String, text:String):Unit
    {
        if(!notificationIsPresent)
        {
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.sym_def_app_icon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(text))
                    //.setDefaults(Notification.)
                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                    .setSilent(true)
                    .setSound(null)
                    .setOngoing(true)


            //Backgroundcolor? setColor und setColorized


            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
            notificationIsPresent=true
        }
    }

    override fun cancelNotificationIfOnePresent(context:Context, id:Int):Unit
    {
        if(notificationIsPresent)
        {
            NotificationManagerCompat.from(context).cancel(id)
            notificationIsPresent=false
        }
    }
}