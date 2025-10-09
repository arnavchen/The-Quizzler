package com.example.thequizzler.ui.screens

import android.icu.lang.UCharacter
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thequizzler.Navigation
import com.example.thequizzler.ui.theme.TheQuizzlerTheme

@Composable
fun HomeScreen(navController: NavController) {

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

    if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
        HorizontalHomeScreen()
    } else {
        VerticalHomeScreen()
    }
}

@Composable
fun VerticalHomeScreen() {

    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "VerticalHomeScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "VerticalHomeScreen Composable DISPOSED")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QuizzlerTitle()
        NameField()
        PlayButton()
    }
}

@Composable
fun HorizontalHomeScreen() {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "HorizontalHomeScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "HorizontalHomeScreen Composable DISPOSED")
        }
    }

    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            QuizzlerTitle()
            NameField()
            PlayButton()
        }

        // Optional: add a decorative or preview image
        // Image(painter = painterResource(R.drawable.quizzler_logo), contentDescription = null)
    }
}

@Composable
fun NameField(modifier: Modifier = Modifier) {
    val name = rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "NameField Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "NameField Composable DISPOSED")
        }
    }

    OutlinedTextField(
        value = name.value,
        onValueChange = { text ->
            name.value = text
        },
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
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun PlayButton() {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "PlayButton Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "PlayButton Composable DISPOSED")
        }
    }

    Button(onClick = {}) {
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


