package com.example.lockscreenmessage

import android.content.Context
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

        activityMainBinding.topAppBar.menu.findItem(R.id.about).setOnMenuItemClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        }

        val persistentSaver:IPersistentSaver=PersistentSaver(getSharedPreferences("settings", Context.MODE_PRIVATE))
        persistentSaver.writeValue(getString(R.string.lockScreenMessageId), 11223344)

        activityMainBinding.saveButton.setOnClickListener {
            persistentSaver.writeValue(getString(R.string.lockScreenMessageTitle), activityMainBinding.lockScreenMessageTitleTextInput.editText?.text.toString())
            persistentSaver.writeValue(getString(R.string.lockScreenMessageContent), activityMainBinding.lockScreenMessageContentTextInput.editText?.text.toString())
            ContextCompat.startForegroundService(
                this,
                Intent(this, NotificationService::class.java)
            )
        }

        activityMainBinding.deleteButton.setOnClickListener {
            activityMainBinding.lockScreenMessageContentTextInput.editText?.text?.clear()
            activityMainBinding.lockScreenMessageTitleTextInput.editText?.text?.clear()
            persistentSaver.removeValue(getString(R.string.lockScreenMessageTitle))
            persistentSaver.removeValue(getString(R.string.lockScreenMessageContent))
            stopService(Intent(this, NotificationService::class.java))
        }
    }

}