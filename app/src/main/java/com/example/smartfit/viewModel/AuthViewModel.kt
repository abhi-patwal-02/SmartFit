package com.example.smartfit.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Authenticated(val userEmail: String?) : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow<AuthState>(AuthState.Idle)
    val uiState: StateFlow<AuthState> = _uiState

    fun checkIfUserIsLoggedIn() {
        val currentUser = auth.currentUser
        _uiState.value = if (currentUser != null) {
            AuthState.Authenticated(currentUser.email)
        } else {
            AuthState.Unauthenticated
        }
    }

    fun login(email: String, password: String) {
        _uiState.value = AuthState.Loading
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _uiState.value = if (task.isSuccessful) {
                        AuthState.Authenticated(auth.currentUser?.email)
                    } else {
                        AuthState.Error(task.exception?.localizedMessage ?: "Login failed")
                    }
                }
        }
    }

    fun signUp(email: String, password: String) {
        _uiState.value = AuthState.Loading
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _uiState.value = if (task.isSuccessful) {
                        AuthState.Authenticated(auth.currentUser?.email)
                    } else {
                        AuthState.Error(task.exception?.localizedMessage ?: "Sign up failed")
                    }
                }
        }
    }

    fun signOut() {
        auth.signOut()
        _uiState.value = AuthState.Unauthenticated
    }
}
