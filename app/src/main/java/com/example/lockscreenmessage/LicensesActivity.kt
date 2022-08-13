package com.example.lockscreenmessage

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.lockscreenmessage.databinding.ActivityLicensesBinding


class LicensesActivity: AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val licencesBinding:ActivityLicensesBinding = ActivityLicensesBinding.inflate(layoutInflater)
        setContentView(licencesBinding.getRoot())

        val webView: WebView = licencesBinding.webview
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.loadUrl("file:///android_asset/licenses.html")
    }
}