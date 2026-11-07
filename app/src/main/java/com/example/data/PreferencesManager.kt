package com.example.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("istighfar_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CURRENT_COUNT = "current_count"
        private const val KEY_TARGET_COUNT = "target_count"
        private const val KEY_TOTAL_ISTIGHFAR = "total_istighfar"
        private const val KEY_COMPLETED_ROUNDS = "completed_rounds"
        private const val KEY_HAPTIC_ENABLED = "haptic_enabled"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_NIGHT_MODE = "night_mode" // "system", "dark", "light"
    }

    var currentCount: Int
        get() = prefs.getInt(KEY_CURRENT_COUNT, 99)
        set(value) = prefs.edit().putInt(KEY_CURRENT_COUNT, value).apply()

    var targetCount: Int
        get() = prefs.getInt(KEY_TARGET_COUNT, 99)
        set(value) = prefs.edit().putInt(KEY_TARGET_COUNT, value).apply()

    var totalIstighfar: Int
        get() = prefs.getInt(KEY_TOTAL_ISTIGHFAR, 0)
        set(value) = prefs.edit().putInt(KEY_TOTAL_ISTIGHFAR, value).apply()

    var completedRounds: Int
        get() = prefs.getInt(KEY_COMPLETED_ROUNDS, 0)
        set(value) = prefs.edit().putInt(KEY_COMPLETED_ROUNDS, value).apply()

    var isHapticEnabled: Boolean
        get() = prefs.getBoolean(KEY_HAPTIC_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_HAPTIC_ENABLED, value).apply()

    var isSoundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND_ENABLED, value).apply()

    var nightModeSetting: String
        get() = prefs.getString(KEY_NIGHT_MODE, "dark") ?: "dark"
        set(value) = prefs.edit().putString(KEY_NIGHT_MODE, value).apply()
}
