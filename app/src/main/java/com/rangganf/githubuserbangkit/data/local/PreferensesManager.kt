package com.rangganf.githubuserbangkit.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Nggawe DataStore kanggo nyimpen preferensi setelan aplikasi.
private val Context.prefDataStore by preferencesDataStore("settings")

// Kelas SettingPreferences digunakake kanggo ngatur preferensi setelan aplikasi.
class SettingPreferences constructor(context: Context) {

    // Initialize DataStore kanggo pilihan setelan.
    private val settingsDataStore = context.prefDataStore

    // Nggawe tombol pilihan kanggo tema (mode peteng utawa padang).
    private val themeKEY = booleanPreferencesKey("theme_setting")

    // Fungsi getThemeSetting digunakake kanggo njupuk preferensi tema minangka Flow sing bisa diamati.
    fun getThemeSetting(): Flow<Boolean> =
        settingsDataStore.data.map { preferences ->
            preferences[themeKEY] ?: false // Njupuk preferensi tema utawa gawe nilai standar liyane seng ora kasedhiya.
        }

    // Fungsi saveThemeSetting digunakake kanggo nyimpen preferensi tema (mode peteng utawa padang) menyang DataStore.
    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        // Ngedit preferensi neng DataStore lan disimpen nilai temane.
        settingsDataStore.edit { preferences ->
            preferences[themeKEY] = isDarkModeActive
        }
    }
}
