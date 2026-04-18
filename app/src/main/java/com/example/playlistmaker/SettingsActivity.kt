package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.back_button)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val buttonShare = findViewById<TextView>(R.id.share_text)
        val buttonCheckSupport = findViewById<TextView>(R.id.support_text)
        val buttonAgreement = findViewById<TextView>(R.id.license_text)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }

        themeSwitcher.isChecked =
            (application as App).getSharedPrefs().getBoolean(DARK_THEME_KEY, false)
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                setType("text/plain")
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_course_url))
            }
            startActivity(shareIntent)
        }

        buttonCheckSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            }
            startActivity(supportIntent)
        }

        buttonAgreement.setOnClickListener {
            val offerIntent = Intent(Intent.ACTION_VIEW).apply {
                data = getString(R.string.practicum_offer).toUri()
            }
            startActivity(offerIntent)
        }
    }

}