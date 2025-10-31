package com.example.thequizzler.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thequizzler.dataPersistence.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// A small interface describing what the UI needs from the ViewModel.
interface SettingsUiModel {
    val isLocationEnabled: StateFlow<Boolean>
    val isOfflineMode: StateFlow<Boolean>
    val measurementSystem: StateFlow<String>

    fun setLocationEnabled(isEnabled: Boolean)
    fun setOfflineMode(isEnabled: Boolean)
    fun setMeasurementSystem(system: String)
}

// The ViewModel takes the repository as a constructor parameter (Dependency Injection)
class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel(), SettingsUiModel {

    // 1. READ the settings from the repository and expose them as StateFlows
    // The UI will "collect" these flows to get the current state.
    override val isLocationEnabled: StateFlow<Boolean> = settingsRepository.isLocationEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    override val isOfflineMode: StateFlow<Boolean> = settingsRepository.isOfflineMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    override val measurementSystem: StateFlow<String> = settingsRepository.measurementSystem
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Met")

    // 2. WRITE functions that the UI will call when the user interacts with a setting
    override fun setLocationEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setLocationEnabled(isEnabled)
        }
    }

    override fun setOfflineMode(isEnabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setOfflineMode(isEnabled)
        }
    }

    override fun setMeasurementSystem(system: String) {
        viewModelScope.launch {
            settingsRepository.setMeasurementSystem(system)
        }
    }
}

class PreviewSettingsUiModel : ViewModel(), SettingsUiModel {
    private val _isLocationEnabled = MutableStateFlow(false)
    private val _isOfflineMode = MutableStateFlow(true)
    private val _measurementSystem = MutableStateFlow("Met")

    override val isLocationEnabled: StateFlow<Boolean> = _isLocationEnabled.asStateFlow()
    override val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()
    override val measurementSystem: StateFlow<String> = _measurementSystem.asStateFlow()

    override fun setLocationEnabled(isEnabled: Boolean) {
        _isLocationEnabled.value = isEnabled
    }

    override fun setOfflineMode(isEnabled: Boolean) {
        _isOfflineMode.value = isEnabled
    }

    override fun setMeasurementSystem(system: String) {
        _measurementSystem.value = system
    }
}
