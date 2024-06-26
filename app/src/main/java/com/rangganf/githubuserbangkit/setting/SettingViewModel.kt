package com.rangganf.githubuserbangkit.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rangganf.githubuserbangkit.data.local.SettingPreferences
import kotlinx.coroutines.launch

// Define a ViewModel class to be used for application settings.
class SettingViewModel(private val pref: SettingPreferences) : ViewModel() {

    // This function is used to return LiveData containing the stored theme settings.
    fun getTheme() = pref.getThemeSetting().asLiveData()

    // This function is used to save the theme setting chosen by the user.
    fun saveTheme(isDark: Boolean) {
        // Start an operation within the ViewModel's scope to make it manageable by the ViewModel.
        viewModelScope.launch {
            // Call the saveThemeSetting() function inside a coroutine to change the theme setting.
            pref.saveThemeSetting(isDark)
        }
    }

    // The Factory class is used to create a ViewModel instance with a dependency on SettingPreferences.
    class Factory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
        // Override the create function to create a SettingViewModel instance with SettingPreferences as a dependency.
        override fun <T : ViewModel> create(modelClass: Class<T>): T = SettingViewModel(pref) as T
    }
}
