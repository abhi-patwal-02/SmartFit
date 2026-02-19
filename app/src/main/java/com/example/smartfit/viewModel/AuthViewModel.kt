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
        _uiState.value =
            if (currentUser != null) AuthState.Authenticated(currentUser.email)
            else AuthState.Unauthenticated
    }

    fun login(email: String, password: String) {
        _uiState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _uiState.value =
                    if (task.isSuccessful)
                        AuthState.Authenticated(auth.currentUser?.email)
                    else
                        AuthState.Error(task.exception?.localizedMessage ?: "Login failed")
            }
    }

    fun signUp(email: String, password: String, name: String) {

        _uiState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener

                    val profile = mapOf(
                        "name" to name,
                        "email" to email,
                        "createdAt" to System.currentTimeMillis()
                    )

                    com.google.firebase.firestore.FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(uid)
                        .collection("profile")
                        .document("main")
                        .set(profile)
                        .addOnSuccessListener {
                            _uiState.value = AuthState.Authenticated(email)
                        }
                        .addOnFailureListener {
                            _uiState.value = AuthState.Error("Profile save failed")
                        }

                } else {
                    _uiState.value =
                        AuthState.Error(task.exception?.localizedMessage ?: "Sign up failed")
                }
            }
    }


    fun signOut() {
        auth.signOut()
        _uiState.value = AuthState.Unauthenticated
    }

    fun clearError() {
        _uiState.value = AuthState.Unauthenticated
    }
}
