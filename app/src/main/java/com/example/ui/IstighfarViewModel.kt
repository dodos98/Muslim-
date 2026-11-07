package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PreferencesManager
import com.example.util.SoundHapticHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class IstighfarUiState(
    val currentCount: Int = 99,
    val targetCount: Int = 99,
    val totalIstighfar: Int = 0,
    val completedRounds: Int = 0,
    val isZeroReached: Boolean = false,
    val isHapticEnabled: Boolean = true,
    val isSoundEnabled: Boolean = true,
    val nightModeSetting: String = "dark", // "dark", "light", "system"
    val showSettingsSheet: Boolean = false,
    val showSayyidIstighfar: Boolean = false,
    val showResetConfirmation: Boolean = false
)

class IstighfarViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferencesManager(application)
    private val soundHapticHelper = SoundHapticHelper(application)

    private val _uiState = MutableStateFlow(
        IstighfarUiState(
            currentCount = prefs.currentCount,
            targetCount = prefs.targetCount,
            totalIstighfar = prefs.totalIstighfar,
            completedRounds = prefs.completedRounds,
            isHapticEnabled = prefs.isHapticEnabled,
            isSoundEnabled = prefs.isSoundEnabled,
            nightModeSetting = prefs.nightModeSetting
        )
    )
    val uiState: StateFlow<IstighfarUiState> = _uiState.asStateFlow()

    fun decrement() {
        val state = _uiState.value
        if (state.currentCount > 0) {
            val nextCount = state.currentCount - 1
            val nextTotal = state.totalIstighfar + 1
            val zeroReached = (nextCount == 0)
            val nextRounds = if (zeroReached) state.completedRounds + 1 else state.completedRounds

            _uiState.update {
                it.copy(
                    currentCount = nextCount,
                    totalIstighfar = nextTotal,
                    completedRounds = nextRounds,
                    isZeroReached = zeroReached
                )
            }

            prefs.currentCount = nextCount
            prefs.totalIstighfar = nextTotal
            prefs.completedRounds = nextRounds

            if (zeroReached) {
                soundHapticHelper.playZeroReachedAlert(state.isHapticEnabled, state.isSoundEnabled)
            } else {
                soundHapticHelper.playTapFeedback(state.isHapticEnabled)
            }
        } else {
            // Already 0, prompt completion alert
            soundHapticHelper.playZeroReachedAlert(state.isHapticEnabled, state.isSoundEnabled)
            _uiState.update { it.copy(isZeroReached = true) }
        }
    }

    fun resetCount() {
        val target = _uiState.value.targetCount
        _uiState.update {
            it.copy(
                currentCount = target,
                isZeroReached = false,
                showResetConfirmation = false
            )
        }
        prefs.currentCount = target
        soundHapticHelper.playTapFeedback(_uiState.value.isHapticEnabled)
    }

    fun setTargetCount(newTarget: Int) {
        _uiState.update {
            it.copy(
                targetCount = newTarget,
                currentCount = newTarget,
                isZeroReached = false
            )
        }
        prefs.targetCount = newTarget
        prefs.currentCount = newTarget
    }

    fun dismissZeroDialog() {
        _uiState.update { it.copy(isZeroReached = false) }
    }

    fun restartNewRound() {
        resetCount()
    }

    fun setHapticEnabled(enabled: Boolean) {
        _uiState.update { it.copy(isHapticEnabled = enabled) }
        prefs.isHapticEnabled = enabled
    }

    fun setSoundEnabled(enabled: Boolean) {
        _uiState.update { it.copy(isSoundEnabled = enabled) }
        prefs.isSoundEnabled = enabled
    }

    fun setNightModeSetting(setting: String) {
        _uiState.update { it.copy(nightModeSetting = setting) }
        prefs.nightModeSetting = setting
    }

    fun toggleNightMode() {
        val current = _uiState.value.nightModeSetting
        val next = if (current == "dark") "light" else "dark"
        setNightModeSetting(next)
    }

    fun setShowSettings(show: Boolean) {
        _uiState.update { it.copy(showSettingsSheet = show) }
    }

    fun setShowSayyidIstighfar(show: Boolean) {
        _uiState.update { it.copy(showSayyidIstighfar = show) }
    }

    fun setShowResetConfirmation(show: Boolean) {
        _uiState.update { it.copy(showResetConfirmation = show) }
    }

    fun resetAllStats() {
        _uiState.update {
            it.copy(
                totalIstighfar = 0,
                completedRounds = 0,
                currentCount = it.targetCount,
                isZeroReached = false,
                showResetConfirmation = false
            )
        }
        prefs.totalIstighfar = 0
        prefs.completedRounds = 0
        prefs.currentCount = _uiState.value.targetCount
    }

    override fun onCleared() {
        super.onCleared()
        soundHapticHelper.release()
    }
}
