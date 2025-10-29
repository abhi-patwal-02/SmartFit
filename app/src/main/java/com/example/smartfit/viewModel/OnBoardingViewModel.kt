package com.example.smartfit.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfit.model.OnboardingModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


sealed class OnboardingState {
    object Idle : OnboardingState()
    object Loading : OnboardingState()
    object Success : OnboardingState()
    data class Error(val message: String) : OnboardingState()
}

class OnboardingViewModel : ViewModel() {

    var currentWeight = MutableStateFlow("")
    var targetWeight = MutableStateFlow("")
    var height = MutableStateFlow("")
    var goal = MutableStateFlow("Lose Weight")

    private val _state = MutableStateFlow<OnboardingState>(OnboardingState.Idle)
    val state: StateFlow<OnboardingState> = _state
    private val _onboardingComplete = MutableStateFlow(false)
    val onboardingComplete: StateFlow<Boolean> = _onboardingComplete

    fun checkOnboardingCompleted(uid: String) {
        val docRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("onboardingDetails")
            .document("main")

        docRef.get()
            .addOnSuccessListener { document ->
                _onboardingComplete.value = document.exists()
            }
            .addOnFailureListener {
                _onboardingComplete.value = false
            }
    }

    fun saveOnboardingDetails(
        uid: String,
        currWt: String,
        targetWt: String,
        height: String,
        goal: String,
        onComplete: () -> Unit
    ) {
        _state.value = OnboardingState.Loading
        val data = OnboardingModel(
            currentWeight = currWt,
            targetWeight = targetWt,
            height = height,
            goal = goal
        ).toMap()

        viewModelScope.launch {
            try {
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .collection("onboardingDetails")
                    .document("main")
                    .set(data)
                    .await()
                _state.value = OnboardingState.Success
                _onboardingComplete.value = true
                onComplete()
            } catch (ex: Exception) {
                _state.value = OnboardingState.Error(ex.message ?: "Save failed")
            }
        }
    }
}
