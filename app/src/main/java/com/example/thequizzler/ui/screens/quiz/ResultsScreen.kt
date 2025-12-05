package com.example.thequizzler.ui.screens.quiz

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.thequizzler.navigation.Screen
import com.example.thequizzler.ui.theme.TheQuizzlerTheme

@Composable
fun ResultsScreen(navController: NavController, quizViewModel: QuizViewModel) {

    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "ResultsScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "ResultsScreen Composable DISPOSED")
        }
    }

    val uiState by quizViewModel.uiState.collectAsState()
    val finalScore = uiState.quizManager?.score ?: 0

    // This LaunchedEffect runs ONCE when the screen is first composed.
    // It's the perfect place to trigger the save operation.
    LaunchedEffect(Unit) {
        quizViewModel.finalizeAndSaveQuiz()
    }

    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        HorizontalResultsScreen(navController = navController, finalScore = finalScore)
    } else {
        VerticalResultsScreen(navController = navController, finalScore = finalScore)
    }
}


// VerticalResultsScreen, HorizontalResultsScreen and their Previews
// remain exactly the same as they were. No changes needed here.
@Composable
fun VerticalResultsScreen(navController: NavController, finalScore: Int) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Trophy emoji or celebration
        Text(
            text = "🏆",
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 80.sp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Score card with elevation
        Card(
            modifier = Modifier
                .shadow(8.dp, shape = MaterialTheme.shapes.large),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your Score",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = finalScore.toString(),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 72.sp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Congratulations!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You really nailed this quiz!",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            ),
            textAlign = TextAlign.Center
        )


        Spacer(modifier = Modifier.height(40.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                } },
                modifier = Modifier
                    .weight(1f)
                    .height(72.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Home",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Button(
                onClick = { navController.navigate(Screen.Leaderboard.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                } },
                modifier = Modifier
                    .weight(1f)
                    .height(72.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Leaderboard,
                        contentDescription = "Leaderboard",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Leaderboard",
                        style = MaterialTheme.typography.labelLarge
                    )
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
            // Trophy emoji
            Text(
                text = "🏆",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 64.sp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Score card
            Card(
                modifier = Modifier
                    .shadow(8.dp, shape = MaterialTheme.shapes.large),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your Score",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = finalScore.toString(),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 64.sp,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Fantastic Work!",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Your performance was outstanding.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Center
            )

        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                } },
                modifier = Modifier
                    .width(140.dp)
                    .height(72.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Home",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Button(
                onClick = { navController.navigate(Screen.Leaderboard.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                } },
                modifier = Modifier
                    .width(140.dp)
                    .height(72.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Leaderboard,
                        contentDescription = "Leaderboard",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Leaderboard",
                        style = MaterialTheme.typography.labelLarge
                    )
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
