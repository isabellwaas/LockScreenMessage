package com.example.lockscreenmessage

import android.content.Context

interface ISingleNotificationHelper
{
    var notificationIsPresent:Boolean

    fun createNotificationChannel(id:String, name:String, description:String, importance:Int, lockscreenVisibility:Int, context: Context):Unit
    fun sendNotificationIfNonePresent(context:Context, channelId:String, notificationId:Int, title:String, text:String):Unit
    fun cancelNotificationIfOnePresent(context:Context, id:Int):Unit
}