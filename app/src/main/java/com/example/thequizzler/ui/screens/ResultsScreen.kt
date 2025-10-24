package com.example.thequizzler.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thequizzler.R
import com.example.thequizzler.ui.theme.TheQuizzlerTheme

@Composable
fun ResultsScreen(navController: NavController, finalScore: Int) {

    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "ResultsScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "ResultsScreen Composable DISPOSED")
        }
    }

    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
        HorizontalResultsScreen(navController = navController, finalScore = finalScore)
    } else {
        VerticalResultsScreen(navController = navController, finalScore = finalScore)
    }
}

@Composable
fun VerticalResultsScreen(navController: NavController, finalScore: Int) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = finalScore.toString(),
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 64.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Great Job!!!",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Buttons Row (Home + Leaderboard)
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.size(width = 120.dp, height = 80.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Home")
                }
            }

            Button(
                onClick = { navController.navigate("leaderboard") },
                modifier = Modifier.size(width = 120.dp, height = 80.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Leaderboard")
                }
            }
        }
    }
}

@Composable
fun HorizontalResultsScreen(navController: NavController, finalScore: Int) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = finalScore.toString(),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 64.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Great Job!!!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium),
                textAlign = TextAlign.Center
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.size(width = 120.dp, height = 80.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Home")
                }
            }

            Button(
                onClick = { navController.navigate("leaderboard") },
                modifier = Modifier.size(width = 120.dp, height = 80.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text("Leaderboard")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerticalResultsScreenPreview() {
    TheQuizzlerTheme {
        VerticalResultsScreen(
            navController = rememberNavController(),
            finalScore = 960
        )
    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun HorizontalResultsScreenPreview() {
    TheQuizzlerTheme {
        HorizontalResultsScreen(
            navController = rememberNavController(),
            finalScore = 960
        )
    }
}
