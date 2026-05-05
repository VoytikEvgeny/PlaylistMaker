package com.example.playlistmaker.setting.ui.activity

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.setting.ui.NavigationEvent
import com.example.playlistmaker.setting.ui.SettingsEvent
import com.example.playlistmaker.setting.ui.viewModel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
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
        viewModel = ViewModelProvider(this, SettingsViewModel.Companion.getViewModelFactory()).get(
            SettingsViewModel::class.java
        )
        setupObservers()
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
            viewModel.updateTheme(checked)
        }
        binding.shareText.setOnClickListener {
            viewModel.getIntent(NavigationEvent.SHARE)
        }
        binding.supportText.setOnClickListener {
            viewModel.getIntent(NavigationEvent.SUPPORT)
        }
        binding.licenseText.setOnClickListener {
            viewModel.getIntent(NavigationEvent.AGREEMENT)
        }
    }

    private fun setupObservers() {
        viewModel.getNavigationEvents().observe(this) { event ->
            when (event) {
                is SettingsEvent.Event -> openApp(event)
                is SettingsEvent.Theme -> updateSwitch(event.isDark)
            }
        }
    }

    private fun openApp(event: SettingsEvent.Event) {
        try {
            startActivity(event.intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, event.errorMessage, Toast.LENGTH_LONG)
                .show()
        }
    }
}