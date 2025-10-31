package com.example.thequizzler.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thequizzler.ui.AppViewModelProvider
import com.example.thequizzler.ui.theme.TheQuizzlerTheme
import java.net.URLEncoder

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {

    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "HomeScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "HomeScreen Composable DISPOSED")
        }
    }

    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    val model: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)

    if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
        HorizontalHomeScreen(
            navController = navController,
            name = model.playerName,
            onNameChange = { model.onNameChange(it) },
            modifier = modifier
        )
    } else {
        VerticalHomeScreen(
            navController = navController,
            name = model.playerName,
            onNameChange = { model.onNameChange(it) },
            modifier = modifier
        )
    }
}

@Composable
fun VerticalHomeScreen(navController: NavController, name: String, onNameChange: (String) -> Unit, modifier: Modifier = Modifier) {

    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "VerticalHomeScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "VerticalHomeScreen Composable DISPOSED")
        }
    }

    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QuizzlerTitle()
        NameField(
            name = name,
            onNameChange = onNameChange,
            modifier = Modifier.padding(top = 16.dp)
        )
        PlayButton(navController = navController, name = name)
    }
}

@Composable
fun HorizontalHomeScreen(navController: NavController, name: String, onNameChange: (String) -> Unit, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "HorizontalHomeScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "HorizontalHomeScreen Composable DISPOSED")
        }
    }

    androidx.compose.foundation.layout.Row(
        modifier = modifier.padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            QuizzlerTitle()
            NameField(
                name = name,
                onNameChange = onNameChange,
                modifier = Modifier.padding(top = 16.dp)
            )
            PlayButton(navController = navController, name = name)
        }
    }
}

@Composable
fun NameField(name: String, onNameChange: (String) -> Unit, modifier: Modifier = Modifier) {

    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "NameField Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "NameField Composable DISPOSED")
        }
    }

    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Enter your name") },
        modifier = modifier
    )
}


@Composable
fun QuizzlerTitle() {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "QuizzlerTitle Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "QuizzlerTitle Composable DISPOSED")
        }
    }

    Text(
        text = "The Quizzler",
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
    )
}


@Composable
fun PlayButton(navController: NavController, name: String) {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "PlayButton Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "PlayButton Composable DISPOSED")
        }
    }

    Button(onClick = {
        val player = URLEncoder.encode(name.ifEmpty { "Player" }, "UTF-8")
        navController.navigate("mock_quiz/$player")
    }) {
        Text("Play")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TheQuizzlerTheme {
        HomeScreen(navController = rememberNavController())
    }
}
