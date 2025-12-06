package com.example.thequizzler.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
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

    LaunchedEffect(Unit) { Log.d("Lifecycle", "HomeScreen CREATED") }
    DisposableEffect(Unit) { onDispose { Log.d("Lifecycle", "HomeScreen DISPOSED") } }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation ==
            android.content.res.Configuration.ORIENTATION_LANDSCAPE

    val model: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)

    HomeScreenLayout(
        navController = navController,
        name = model.playerName,
        onNameChange = model::onNameChange,
        isLandscape = isLandscape,
        modifier = modifier
    )
}

@Composable
fun HomeScreenLayout(
    navController: NavController,
    name: String,
    onNameChange: (String) -> Unit,
    isLandscape: Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    if (isLandscape) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                QuizzlerTitle()
                NameField(
                    name = name,
                    onNameChange = onNameChange,
                    modifier = Modifier.padding(top = 16.dp)
                )
                PlayButton(
                    navController = navController,
                    name = name,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            QuizzlerTitle()
            NameField(
                name = name,
                onNameChange = onNameChange,
                modifier = Modifier.padding(top = 32.dp)
            )
            PlayButton(
                navController = navController,
                name = name,
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}

@Composable
fun NameField(name: String, onNameChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Enter your name") },
        modifier = modifier
            .fillMaxWidth(0.8f)
            .semantics {
                contentDescription = if (name.isEmpty()) {
                    "Name input field, empty. Enter your name to personalize the quiz"
                } else {
                    "Name input field. Current name: $name"
                }
            },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}

@Composable
fun QuizzlerTitle() {
    Text(
        text = "The Quizzler",
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.semantics {
            heading()
        }
    )
}

@Composable
fun PlayButton(navController: NavController, name: String, modifier: Modifier = Modifier) {
    val playerName = name.ifEmpty { "Player" }

    Button(
        onClick = {
            val player = URLEncoder.encode(playerName, "UTF-8")
            navController.navigate("quiz_flow/$player")
        },
        modifier = modifier
            .fillMaxWidth(0.6f)
            .padding(horizontal = 32.dp)
            .semantics {
                contentDescription = "Start quiz as $playerName. Double tap to begin playing"
            },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Play",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewHomePortrait() {
    TheQuizzlerTheme { HomeScreen(navController = rememberNavController()) }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 360)
@Composable
fun PreviewHomeLandscape() {
    TheQuizzlerTheme { HomeScreen(navController = rememberNavController()) }
}
