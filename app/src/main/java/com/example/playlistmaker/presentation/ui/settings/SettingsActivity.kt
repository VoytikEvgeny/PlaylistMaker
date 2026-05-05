package com.example.playlistmaker.presentation.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {
    private val sharedPrefs = Creator.getThemeManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        val themeSwitch = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val isNightModeEnabled = sharedPrefs.isDarkThemeEnabled(this)
        themeSwitch.isChecked = isNightModeEnabled


        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefs.setDarkTheme(true,this)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefs.setDarkTheme(false,this)
            }
        }

        findViewById<MaterialTextView>(R.id.share_text).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.android_course_url)
                )
            }
            startActivity(Intent.createChooser(intent, getString(R.string.app_share)))
        }

        findViewById<MaterialTextView>(R.id.support_text).setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT,getString(R.string.email_text))
            }
            startActivity(intent)
        }

        findViewById<MaterialTextView>(R.id.license_text).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = getString(R.string.practicum_offer).toUri()
            }
            startActivity(intent)
        }

        findViewById<Toolbar>(R.id.back_button).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

}