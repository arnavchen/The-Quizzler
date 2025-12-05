package com.example.thequizzler.ui.screens.settings

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thequizzler.ui.AppViewModelProvider
import com.example.thequizzler.ui.screens.home.HomeScreen
import com.example.thequizzler.ui.theme.TheQuizzlerTheme

@Composable
fun SettingsScreen(navController: NavController) {
    LaunchedEffect(Unit) { Log.d("Lifecycle", "SettingsScreen CREATED") }

    val model: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    SettingsLayout(
        settingsUiModel = model,
        isLandscape = isLandscape
    )
}

@Composable
fun SettingsLayout(settingsUiModel: SettingsUiModel, isLandscape: Boolean) {
    val scrollState = rememberScrollState()

    val containerModifier =
        if (isLandscape)
            Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 16.dp)
        else
            Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 16.dp)

    Column(
        modifier = containerModifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Settings",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 28.dp)
        )

        SettingsOptions(settingsUiModel)
    }
}

@Composable
fun SettingsOptions(settingsUiModel: SettingsUiModel) {
    val locationEnabled by settingsUiModel.isLocationEnabled.collectAsState(initial = false)
    val offlineMode by settingsUiModel.isOfflineMode.collectAsState(initial = true)
    val measurementSystem by settingsUiModel.measurementSystem.collectAsState(initial = "Met")

    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SettingToggleCard(
            title = "Location Questions",
            icon = Icons.Filled.LocationOn,
            checked = locationEnabled,
            onCheckedChange = settingsUiModel::setLocationEnabled
        )

        SettingToggleCard(
            title = "Offline Mode",
            icon = Icons.Filled.OfflinePin,
            checked = offlineMode,
            onCheckedChange = settingsUiModel::setOfflineMode
        )

        MeasurementSystemCard(
            selectedSystem = measurementSystem,
            onSystemSelected = settingsUiModel::setMeasurementSystem
        )
    }
}

@Composable
fun MeasurementSystemCard(
    selectedSystem: String,
    onSystemSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Straighten,
                    contentDescription = "Measurement System",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Measurement System",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                MeasurementSystemButton(
                    text = "Imperial",
                    selected = selectedSystem == "Imp",
                    onClick = { onSystemSelected("Imp") }
                )
                Spacer(modifier = Modifier.width(12.dp))
                MeasurementSystemButton(
                    text = "Metric",
                    selected = selectedSystem == "Met",
                    onClick = { onSystemSelected("Met") }
                )
            }
        }
    }
}

@Composable
fun SettingToggleCard(
    title: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
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
                MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .defaultMinSize(minWidth = 100.dp)
            .padding(4.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            color = if (selected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurface
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
