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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartfit.debug.ExerciseUploader
import com.example.smartfit.debug.FoodUploader
import com.example.smartfit.debug.HistorySeeder2
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
import com.google.android.filament.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Auto-seed exercises and foods if collections are empty
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        db.collection("exercises").limit(1).get().addOnSuccessListener { snap ->
            if (snap.isEmpty) {
                ExerciseUploader.uploadExercises()
            }
        }
        db.collection("foods").limit(1).get().addOnSuccessListener { snap ->
            if (snap.isEmpty) {
                FoodUploader.uploadFoods()
            }
        }

        Utils.init()

        setContent {
            SmartFitTheme {

                val authViewModel: AuthViewModel = viewModel()
                val onboardingViewModel: OnboardingViewModel = viewModel()

                AuthGate(authViewModel, onboardingViewModel)
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
    val context = LocalContext.current

    var splashFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authViewModel.checkIfUserIsLoggedIn()
        delay(1500)
        splashFinished = true
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            FirebaseAuth.getInstance().currentUser?.uid?.let {
                onboardingViewModel.checkOnboardingCompleted(it)
            }
        }
    }

    if (!splashFinished) {
        SplashScreen()
        return
    }

    when (authState) {

        is AuthState.Error -> {
            val errorMsg = (authState as AuthState.Error).message
            LaunchedEffect(errorMsg) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                authViewModel.clearError()
            }
            SignupLoginScreen(authViewModel)
        }

        is AuthState.Loading -> LoadingScreen()

        is AuthState.Authenticated -> {
            //HistorySeeder2.seed() // uncomment to seed 4 extra days of history
            
            if (!onboardingComplete) {
                OnBoardingScreen(onComplete = {
                    FirebaseAuth.getInstance().currentUser?.uid?.let {
                        onboardingViewModel.checkOnboardingCompleted(it)
                    }
                })
            } else {
                MainScreen(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            }
        }

        is AuthState.Unauthenticated -> SignupLoginScreen(authViewModel)

        AuthState.Idle -> SplashScreen()
    }
}
