package com.example.thequizzler.ui.screens.settings

import android.content.res.Configuration
import android.util.Log
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

    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        HorizontalSettingsScreen()
    } else {
        VerticalSettingsScreen()
    }
}

// Vertical Settings
@Composable
fun VerticalSettingsScreen() {
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

        SettingsOptions()
    }
}

@Composable
fun HorizontalSettingsScreen() {
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

            SettingsOptions()
        }
    }
}

@Composable
fun SettingsOptions() {
    var locationEnabled by rememberSaveable { mutableStateOf(false) }
    var offlineMode by rememberSaveable { mutableStateOf(true) }
    var measurementSystem by rememberSaveable { mutableStateOf("Met") }

    SettingToggleRow(
        title = "Location Questions",
        checked = locationEnabled,
        onCheckedChange = { locationEnabled = it }
    )

    SettingToggleRow(
        title = "Offline Mode",
        checked = offlineMode,
        onCheckedChange = { offlineMode = it }
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
            onClick = { measurementSystem = "Imp" }
        )
        Spacer(modifier = Modifier.width(8.dp))
        MeasurementSystemButton(
            text = "Met",
            selected = measurementSystem == "Met",
            onClick = { measurementSystem = "Met" }
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
