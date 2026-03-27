package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.back_button)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()

            finish()
        }

        val buttonShare = findViewById<TextView>(R.id.share_text)
        buttonShare.setOnClickListener {

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                setType("text/plain")
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_course_url))
            }

            startActivity(shareIntent)

        }

        val buttonCheckSupport = findViewById<TextView>(R.id.support_text)
        buttonCheckSupport.setOnClickListener {

            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            }
            startActivity(supportIntent)
        }

        val buttonAgreement = findViewById<TextView>(R.id.license_text)
        buttonAgreement.setOnClickListener {
            val offerIntent = Intent(Intent.ACTION_VIEW).apply {
                data = getString(R.string.practicum_offer).toUri()
            }
            startActivity(offerIntent)
        }

    }

}