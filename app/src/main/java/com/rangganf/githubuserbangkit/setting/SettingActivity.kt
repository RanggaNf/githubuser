package com.rangganf.githubuserbangkit.setting

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.rangganf.githubuserbangkit.data.local.SettingPreferences
import com.rangganf.githubuserbangkit.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private val viewModel by viewModels<SettingViewModel> {
        // Create a viewModel instance with a factory that requires SettingPreferences
        SettingViewModel.Factory(SettingPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Connect the ActivitySettingBinding layout with the XML layout
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Viewing button in ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Observe theme changes stored in the ViewModel
        viewModel.getTheme().observe(this) { isDarkTheme ->
            if (isDarkTheme) {
                // If dark theme is selected, set the button text to "Dark Theme" and set night mode
                binding.switchTheme.text = "Dark Theme"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // If a light theme is selected, set the button text to "Light Theme" and set the light mode
                binding.switchTheme.text = "Light Theme"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            // This is to set the switch status according to the selected theme
            binding.switchTheme.isChecked = isDarkTheme
        }

        // Add a listener for theme switches that will save theme changes to the ViewModel
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveTheme(isChecked)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Close The aktivity if Pushbutton back in ActionBar
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
