package com.example.thequizzler.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thequizzler.dataPersistence.repositories.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// The ViewModel takes the repository as a constructor parameter (Dependency Injection)
class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    // 1. READ the settings from the repository and expose them as StateFlows
    // The UI will "collect" these flows to get the current state.
    val isLocationEnabled: StateFlow<Boolean> = settingsRepository.isLocationEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isOfflineMode: StateFlow<Boolean> = settingsRepository.isOfflineMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val measurementSystem: StateFlow<String> = settingsRepository.measurementSystem
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Met")

    // 2. WRITE functions that the UI will call when the user interacts with a setting
    fun setLocationEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setLocationEnabled(isEnabled)
        }
    }

    fun setOfflineMode(isEnabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setOfflineMode(isEnabled)
        }
    }

    fun setMeasurementSystem(system: String) {
        viewModelScope.launch {
            settingsRepository.setMeasurementSystem(system)
        }
    }
}
