package com.example.lockscreenmessage

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class NotificationHelper(var notificationIsPresent:Boolean = false)
{
    fun createNotificationChannel(id:String, name:String, description:String, importance:Int, lockscreenVisibility:Int, context:Context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val notificationChannel = NotificationChannel(id, name, importance)
            notificationChannel.setDescription(description)
            notificationChannel.lockscreenVisibility=lockscreenVisibility

            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    fun sendNotification(context:Context, channelId:String, notificationId:Int, title:String, text:String)
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
            val notification:Notification=builder.build().apply {
                //flags = Notification.FLAG_NO_CLEAR
            }

            NotificationManagerCompat.from(context).notify(notificationId, notification)
            notificationIsPresent=true
        }
    }

    fun cancelNotification(context:Context, id:Int)
    {
        if(notificationIsPresent)
        {
            NotificationManagerCompat.from(context).cancel(id)
            notificationIsPresent=false
        }
    }
}