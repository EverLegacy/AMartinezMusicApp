package com.example.amartinezmusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.amartinezmusicapp.navigation.AppNavigation
import com.example.amartinezmusicapp.ui.theme.AMartinezMusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AMartinezMusicApp()
        }
    }
}

@Composable
fun AMartinezMusicApp() {
    AMartinezMusicAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            AppNavigation()
        }
    }
}
