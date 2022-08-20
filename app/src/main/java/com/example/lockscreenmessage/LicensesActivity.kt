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

        val webView: WebView = licencesBinding.webview
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.loadUrl("file:///android_asset/licenses.html")

        setSupportActionBar(licencesBinding.topAppBar)
        licencesBinding.topAppBar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })

        if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            //Code to enable force dark using FORCE_DARK_ON and select force dark strategy
            if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                WebSettingsCompat.setForceDark(
                    licencesBinding.webview.getSettings(),
                    WebSettingsCompat.FORCE_DARK_ON
                );
            }
        }
    }
}