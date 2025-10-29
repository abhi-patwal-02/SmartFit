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
import com.example.smartfit.view.screens.OnBoardingScreen
import com.example.smartfit.view.screens.SignupLoginScreen
import com.example.smartfit.view.screens.SplashScreen
import com.example.smartfit.viewModel.AuthState
import com.example.smartfit.viewModel.AuthViewModel
import com.example.smartfit.viewModel.OnboardingViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartFitTheme {
                // Obtain your AuthViewModel using viewModel()
                val authViewModel: AuthViewModel = viewModel()
                val onboardingViewModel: OnboardingViewModel = viewModel()
                // Entry point for all authentication-based navigation
                AuthGate(
                    authViewModel = authViewModel,
                    onboardingViewModel = onboardingViewModel
                )
            }
        }
    }
}

@Composable
fun AuthGate(
    authViewModel: AuthViewModel = viewModel(),
    onboardingViewModel: OnboardingViewModel = viewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val onboardingComplete by onboardingViewModel.onboardingComplete.collectAsState()
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val context = LocalContext.current

    // Ensure user login state is checked once!
    LaunchedEffect(Unit) {
        authViewModel.checkIfUserIsLoggedIn()
    }

    // After authentication success, check onboarding details
    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated && uid != null) {
            onboardingViewModel.checkOnboardingCompleted(uid)
        }
    }

    if (authState is AuthState.Error) {
        val errorMsg = (authState as AuthState.Error).message
        LaunchedEffect(errorMsg) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
        }
        SignupLoginScreen(authViewModel = authViewModel)
    } else if (authState is AuthState.Loading) {
        LoadingScreen()
    } else if (authState is AuthState.Authenticated && !onboardingComplete) {
        OnBoardingScreen(onComplete = {
            // On save complete: update onboardingComplete to true so the next recomposition shows MainScreen
            uid?.let{
                onboardingViewModel.checkOnboardingCompleted(it)
            }
        })
    } else if (authState is AuthState.Authenticated && onboardingComplete) {
        MainScreen()
    } else if (authState is AuthState.Unauthenticated) {
        SignupLoginScreen(authViewModel = authViewModel)
    } else if (authState == AuthState.Idle) {
        SplashScreen()
    }

}
