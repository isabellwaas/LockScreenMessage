package com.example.lockscreenmessage

import android.app.Notification
import android.content.Context

interface ISingleNotificationHelper
{
    var notificationIsPresent:Boolean

    fun createNotificationChannel(id:String, name:String, description:String, importance:Int, lockscreenVisibility:Int, context: Context):Unit
    fun buildNotificationWithPublicVersion(context:Context, channelId:String, notificationId:Int, title:String, text:String, priority: Int): Notification
    fun sendNotificationIfNonePresent(context:Context, channelId:String, notificationId:Int, title:String, text:String, priority:Int):Unit
    fun cancelNotificationIfOnePresent(context:Context, id:Int):Unit
}