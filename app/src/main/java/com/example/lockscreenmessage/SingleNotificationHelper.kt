package com.example.lockscreenmessage

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class SingleNotificationHelper(override var notificationIsPresent: Boolean = false):ISingleNotificationHelper
{
    override fun createNotificationChannel(id:String, name:String, description:String, importance:Int, lockscreenVisibility:Int, context:Context):Unit
    {
        val notificationChannel = NotificationChannel(id, name, importance)
        notificationChannel.description = description
        notificationChannel.setSound(null, null)
        notificationChannel.lockscreenVisibility=lockscreenVisibility

        NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
    }

    override fun buildNotificationWithPublicVersion(context:Context, channelId:String, notificationId:Int, title:String, text:String, priority: Int, icon:Int):Notification
    {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(priority)
            .setSilent(true)
            .setSound(null)
            .setOngoing(true)

        builder.setPublicVersion(builder.build())
        return builder.build()
    }

    override fun sendNotificationIfNonePresent(context:Context, channelId:String, notificationId:Int, title:String, text:String, priority: Int, icon:Int):Unit
    {
        if(!notificationIsPresent)
        {
            buildNotificationWithPublicVersion(context, channelId, notificationId, title, text, priority, icon).let{
                NotificationManagerCompat.from(context).notify(notificationId, it)
                notificationIsPresent = true
            }
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