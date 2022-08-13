package com.example.lockscreenmessage

import android.content.SharedPreferences

class PersistentSaver(override val sharedPreferences: SharedPreferences):IPersistentSaver
{
    override fun readValue(key:String, defaultValue:String?):String
    {
        return this.sharedPreferences.getString(key, defaultValue) ?: ""
    }

    override fun readValue(key: String, defaultValue: Int): Int {
        return this.sharedPreferences.getInt(key, defaultValue)
    }

    override fun writeValue(key:String, value:String):Unit
    {
        val editor:SharedPreferences.Editor=this.sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun writeValue(key: String, value: Int) {
        val editor:SharedPreferences.Editor=this.sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    override fun removeValue(key:String):Unit
    {
        val editor:SharedPreferences.Editor=this.sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}