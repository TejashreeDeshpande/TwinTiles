package com.example.twintiles

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
import com.example.twintiles.ui.screen.GridScreen
import com.example.twintiles.ui.theme.TwinTilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TwinTilesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GridScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
