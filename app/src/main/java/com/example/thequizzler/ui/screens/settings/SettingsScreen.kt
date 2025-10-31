package com.example.thequizzler.ui.screens.settings

import android.content.res.Configuration
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thequizzler.ui.theme.TheQuizzlerTheme

@Composable
fun SettingsScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "SettingsScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "SettingsScreen Composable DISPOSED")
        }
    }

    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    // Provide a SettingsUiModel: real ViewModel at runtime, preview model in Compose preview
    val settingsUiModel: SettingsUiModel = run {
        val inspection = LocalInspectionMode.current
        if (inspection) {
            remember { PreviewSettingsUiModel() }
        } else {
            val appContext = LocalContext.current.applicationContext
            val repository = (appContext as com.example.thequizzler.QuizzlerApplication).container.settingsRepository
            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return SettingsViewModel(repository) as T
                }
            }
            viewModel<SettingsViewModel>(factory = factory)
        }
    }

    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        HorizontalSettingsScreen(settingsUiModel)
    } else {
        VerticalSettingsScreen(settingsUiModel)
    }
}

// Vertical Settings
@Composable
fun VerticalSettingsScreen(settingsUiModel: SettingsUiModel) {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "VerticalSettingsScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "VerticalSettingsScreen Composable DISPOSED")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Settings",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        SettingsOptions(settingsUiModel)
    }
}

@Composable
fun HorizontalSettingsScreen(settingsUiModel: SettingsUiModel) {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "HorizontalSettingsScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "HorizontalSettingsScreen Composable DISPOSED")
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            SettingsOptions(settingsUiModel)
        }
    }
}

@Composable
fun SettingsOptions(settingsUiModel: SettingsUiModel) {
    val locationEnabled by settingsUiModel.isLocationEnabled.collectAsState(initial = false)
    val offlineMode by settingsUiModel.isOfflineMode.collectAsState(initial = true)
    val measurementSystem by settingsUiModel.measurementSystem.collectAsState(initial = "Met")

    SettingToggleRow(
        title = "Location Questions",
        checked = locationEnabled,
        onCheckedChange = { settingsUiModel.setLocationEnabled(it) }
    )

    SettingToggleRow(
        title = "Offline Mode",
        checked = offlineMode,
        onCheckedChange = { settingsUiModel.setOfflineMode(it) }
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "Measurement System",
        fontSize = 18.sp,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MeasurementSystemButton(
            text = "Imp",
            selected = measurementSystem == "Imp",
            onClick = { settingsUiModel.setMeasurementSystem("Imp") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        MeasurementSystemButton(
            text = "Met",
            selected = measurementSystem == "Met",
            onClick = { settingsUiModel.setMeasurementSystem("Met") }
        )
    }
}

@Composable
fun SettingToggleRow(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 18.sp)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun MeasurementSystemButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .defaultMinSize(minWidth = 80.dp)
    ) {
        Text(
            text = text,
            color = if (selected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    TheQuizzlerTheme {
        SettingsScreen(navController = rememberNavController())
    }
}
