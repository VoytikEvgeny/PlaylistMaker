package com.example.playlistmaker

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.back_button)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()

            finish()
        }
    }

}