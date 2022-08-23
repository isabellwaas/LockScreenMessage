package com.example.lockscreenmessage

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.example.lockscreenmessage.databinding.ActivityLicensesBinding


class LicensesActivity: AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val licencesBinding:ActivityLicensesBinding = ActivityLicensesBinding.inflate(layoutInflater)
        setContentView(licencesBinding.getRoot())

        //Set appbar action
        setSupportActionBar(licencesBinding.topAppBar)
        licencesBinding.topAppBar.setNavigationOnClickListener { finish() }

        //Set webview action
        val webView: WebView = licencesBinding.webview
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.loadUrl("file:///android_asset/licenses.html")

        //Set webview colors according to theme
        if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                WebSettingsCompat.setForceDark(
                    licencesBinding.webview.getSettings(),
                    WebSettingsCompat.FORCE_DARK_ON
                );
            }
        }
    }
}