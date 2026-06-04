package com.example.playlistmaker.setting.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.setting.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.themeSwitcher.isChecked = viewModel.isDarkThemeOn()
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.changeTheme(checked)
        }
        binding.shareText.setOnClickListener {
            viewModel.doShare(getString(R.string.android_course_url))
        }

        binding.supportText.setOnClickListener {
            viewModel.doWrightTechSupport(
                arrayOf(getString(R.string.my_email)),
                getString(R.string.email_subject),
                getString(R.string.email_text)
            )
        }

        binding.licenseText.setOnClickListener {
            viewModel.showAgreement(getString(R.string.practicum_offer))
        }
        binding.backButton.setNavigationOnClickListener {
            finish()
        }

    }
}