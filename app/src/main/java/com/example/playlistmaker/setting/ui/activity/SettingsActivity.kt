package com.example.playlistmaker.setting.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.setting.domain.ThemeSettings
import com.example.playlistmaker.setting.ui.viewModel.SettingsViewModel
import com.example.playlistmaker.sharing.domain.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupClickListeners()
    }

    private fun updateSwitch(isDark: Boolean) {
        binding.themeSwitcher.isChecked = isDark
    }

    private fun setupClickListeners() {
        binding.backButton.setNavigationOnClickListener {
            finish()
        }
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.setCurrentDarkThemeState(ThemeSettings(checked))
        }
        viewModel.getDarkThemeState().observe(this) { state ->
            renderDarkThemeState(state)
        }
        binding.shareText.setOnClickListener {
            viewModel.shareApp(this.getString(R.string.android_course_url))
        }
        binding.supportText.setOnClickListener {
            viewModel.openSupport(
                EmailData(
                    arrayOf(this.getString(R.string.my_email)),
                    this.getString(R.string.email_subject),
                    this.getString(R.string.email_text)
                )
            )
        }
        binding.licenseText.setOnClickListener {
            viewModel.userAgreement(this.getString(R.string.practicum_offer))
        }
    }

    fun renderDarkThemeState(state: ThemeSettings) {
        AppCompatDelegate.setDefaultNightMode(
            if (state.isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        updateSwitch(state.isDark)
    }
}