package com.focusmode.app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences("focus_mode_prefs", MODE_PRIVATE)

        webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.addJavascriptInterface(AndroidBridge(), "AndroidBridge")
        webView.loadUrl("file:///android_asset/index.html")
        setContentView(webView)
    }

    inner class AndroidBridge {
        @JavascriptInterface
        fun startBlocking(mode: String) {
            prefs.edit().putBoolean("session_active", true).putString("block_mode", mode).apply()
        }

        @JavascriptInterface
        fun stopBlocking() {
            prefs.edit().putBoolean("session_active", false).apply()
        }

        @JavascriptInterface
        fun openUsageAccessSettings() {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }

        @JavascriptInterface
        fun openAccessibilitySettings() {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }
}
