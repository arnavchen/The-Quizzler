package com.example.thequizzler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.thequizzler.ui.theme.TheQuizzlerTheme
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheQuizzlerTheme {
                Scaffold { innerPadding ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()
                        .padding(innerPadding)) {
                        AppName()
                        NameField()
                        PlayButton()
                    }
                }
            }
        }
    }
}

@Composable
fun NameField(modifier: Modifier = Modifier) {
    var name = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        value = name.value,
        onValueChange = { text->
            name.value = text
        },
        modifier = modifier
    )
}

@Composable
fun AppName(){
    Text(
        text="The Quizzler",
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun PlayButton(){
    Button(onClick = {}) {
        Text("Play")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheQuizzlerTheme {
        Scaffold { innerPadding ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()
                .padding(innerPadding)) {
                AppName()
                NameField()
                PlayButton()
            }
        }
    }
}