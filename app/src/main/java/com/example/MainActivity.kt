package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.MainScreen
import com.example.ui.state.AppThemeMode
import com.example.ui.state.MainViewModel
import com.example.ui.state.NavDestination
import com.example.ui.theme.BambooTheme

class MainActivity : ComponentActivity() {

  private val mainViewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Handle deep-link from notification
    intent.getStringExtra("NAV_TARGET")?.let { target ->
        if (target == "SCHEDULE") {
            mainViewModel.selectDestination(NavDestination.SCHEDULE)
        }
    }

    setContent {
      val uiState by mainViewModel.uiState.collectAsState()
      val darkTheme = when (uiState.themeMode) {
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
      }

      BambooTheme(
        darkTheme = darkTheme,
        themeColor = uiState.themeColor
      ) {
        MainScreen(
          viewModel = mainViewModel,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}
