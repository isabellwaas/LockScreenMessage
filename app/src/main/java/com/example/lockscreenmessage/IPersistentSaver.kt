package com.example.lockscreenmessage

import android.content.SharedPreferences

interface IPersistentSaver
{
    val sharedPreferences:SharedPreferences

    fun readValue(key:String, defaultValue:String?):String?
    fun writeValue(key:String, value:String):Unit
    fun readValue(key:String, defaultValue:Boolean):Boolean
    fun writeValue(key:String, value:Boolean):Unit
    fun readValue(key:String, defaultValue:Int):Int
    fun writeValue(key:String, value:Int):Unit
    fun removeValue(key:String):Unit
}