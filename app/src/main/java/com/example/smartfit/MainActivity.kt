package com.example.smartfit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartfit.ui.theme.SmartFitTheme
import com.example.smartfit.view.screens.DashboardScreen
import com.example.smartfit.view.screens.LoadingScreen
import com.example.smartfit.view.screens.MainScreen
import com.example.smartfit.view.screens.SignupLoginScreen
import com.example.smartfit.view.screens.SplashScreen
import com.example.smartfit.viewModel.AuthState
import com.example.smartfit.viewModel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartFitTheme {
                // Obtain your AuthViewModel using viewModel()
                val authViewModel: AuthViewModel = viewModel()
                // Entry point for all authentication-based navigation
                AuthGate(authViewModel = authViewModel)
            }
        }
    }
}

@Composable
fun AuthGate(authViewModel: AuthViewModel = viewModel()) {
    val authState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Ensure user login state is checked once!
    LaunchedEffect(Unit) {
        authViewModel.checkIfUserIsLoggedIn()
    }

    if (authState is AuthState.Error) {
        val errorMsg = (authState as AuthState.Error).message
        LaunchedEffect(errorMsg) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
        }
        SignupLoginScreen(authViewModel = authViewModel)
    } else when (authState) {
        is AuthState.Loading -> LoadingScreen()
        is AuthState.Authenticated -> MainScreen()
        is AuthState.Unauthenticated -> SignupLoginScreen(authViewModel = authViewModel)
        AuthState.Idle -> SplashScreen()
        else -> {}
    }
}
