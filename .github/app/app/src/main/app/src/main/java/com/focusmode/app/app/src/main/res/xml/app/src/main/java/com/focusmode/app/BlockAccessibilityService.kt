package com.focusmode.app

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class BlockAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val prefs = getSharedPreferences("focus_mode_prefs", MODE_PRIVATE)
        val sessionActive = prefs.getBoolean("session_active", false)
        val mode = prefs.getString("block_mode", "none")

        if (!sessionActive || mode == "none") return

        val pkg = event?.packageName?.toString() ?: return
        if (pkg == packageName) return
        if (pkg.contains("dialer") || pkg.contains("phone") || pkg.contains("systemui")) return

        if (mode == "whole") {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    override fun onInterrupt() {}
}
