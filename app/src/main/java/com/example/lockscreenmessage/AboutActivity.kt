package com.example.lockscreenmessage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.lockscreenmessage.databinding.ActivityAboutBinding


class AboutActivity: AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        
        val aboutBinding: ActivityAboutBinding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(aboutBinding.getRoot())

        // Set appbar action
        setSupportActionBar(aboutBinding.topAppBar)
        aboutBinding.topAppBar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })

        // Set developer button action
        aboutBinding.developer.setOnClickListener { view ->
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/isabellwaas")
                )
            )
        }

        // Set licences button action
        aboutBinding.licences.setOnClickListener { view: View? ->
            startActivity(Intent(applicationContext, LicensesActivity::class.java))
        }
    }
}