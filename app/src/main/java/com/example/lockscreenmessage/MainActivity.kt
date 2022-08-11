package com.example.lockscreenmessage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lockscreenmessage.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity()
{
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        ContextCompat.startForegroundService(this,  Intent(this, NotificationService::class.java))

        //Stop Service
        //stopService(Intent(this, NotificationService::class.java))
    }

}