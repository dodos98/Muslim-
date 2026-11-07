package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.IstighfarScreen
import com.example.ui.IstighfarViewModel
import com.example.ui.theme.IstighfarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: IstighfarViewModel = viewModel()
            val state by viewModel.uiState.collectAsState()

            val systemDark = isSystemInDarkTheme()
            val isDarkTheme = when (state.nightModeSetting) {
                "dark" -> true
                "light" -> false
                else -> systemDark
            }

            IstighfarTheme(darkTheme = isDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    IstighfarScreen(
                        viewModel = viewModel,
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }
    }
}
