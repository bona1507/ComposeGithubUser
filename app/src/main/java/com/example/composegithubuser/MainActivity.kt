package com.example.composegithubuser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.composegithubuser.ui.SplashScreen
import com.example.composegithubuser.ui.theme.MainAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var timeOutSplash by remember { mutableStateOf(false) }
                    if (timeOutSplash) {
                        MainApp()
                    } else {
                        SplashScreen()
                        CountDownSplash(onTimeout = { timeOutSplash = true })
                    }
                }
            }
        }
    }
}

@Composable
fun CountDownSplash(onTimeout: () -> Unit) {
    var timer by remember { mutableIntStateOf(3) }
    LaunchedEffect(key1 = timer) {
        while (timer > 0) {
            delay(1000)
            timer--
        }
        onTimeout()
    }
}
