package com.example.thequizzler.ui.screens.loading

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thequizzler.ui.theme.AppSpacing
import com.example.thequizzler.ui.theme.TheQuizzlerTheme


@Composable
fun LoadingScreen(
    title: String = "The Quizzler",
    message: String? = null,
    generationFailed: Boolean = false,
    onReturnHome: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppSpacing.large),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(AppSpacing.medium))

            if (generationFailed) {
                Text(
                    text = "Sorry, we couldn't generate a full quiz for you at this time. Please try again later.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.size(AppSpacing.large))
                Button(onClick = onReturnHome) {
                    Text("Return to Home")
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(56.dp)
                        .rotate(angle.value),
                    color = MaterialTheme.colorScheme.primary
                )

                message?.let {
                    Spacer(modifier = Modifier.size(AppSpacing.medium))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

/**
 * Small helper to show loading UI conditionally. When integrating into navigation or
 * view models you can wrap a screen's content with this to show loading state.
 */
@Composable
fun LoadingHost(isLoading: Boolean, loadingMessage: String? = null, content: @Composable () -> Unit) {
    if (isLoading) {
        LoadingScreen(message = loadingMessage)
    } else {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreviewLight() {
    TheQuizzlerTheme(darkTheme = false) {
        LoadingScreen(message = "Preparing your quiz...")
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreviewDark() {
    TheQuizzlerTheme(darkTheme = true) {
        LoadingScreen(message = "Preparing your quiz...")
    }
}
