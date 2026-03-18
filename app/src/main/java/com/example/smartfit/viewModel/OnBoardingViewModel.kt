package com.example.smartfit.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfit.model.OnboardingModel
import com.example.smartfit.model.ProfileModel
import com.google.firebase.auth.FirebaseAuth
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

    private val _state = MutableStateFlow<OnboardingState>(OnboardingState.Idle)
    val state: StateFlow<OnboardingState> = _state

    private val _onboardingComplete = MutableStateFlow(false)
    val onboardingComplete: StateFlow<Boolean> = _onboardingComplete

    private val _profileData = MutableStateFlow<ProfileModel?>(null)
    val profileData: StateFlow<ProfileModel?> = _profileData


    fun fetchProfile(uid: String) {

        val db = FirebaseFirestore.getInstance()

        val email = FirebaseAuth.getInstance().currentUser?.email ?: ""

        db.collection("users")
            .document(uid)
            .collection("profile")
            .document("main")
            .get()
            .addOnSuccessListener { profileDoc ->

                val name = profileDoc.getString("name") ?: "User"

                db.collection("users")
                    .document(uid)
                    .collection("onboardingDetails")
                    .document("main")
                    .get()
                    .addOnSuccessListener { onboardDoc ->

                        _profileData.value = ProfileModel(
                            email = email,
                            currentWeight = onboardDoc.getString("currentWeight") ?: "-",
                            targetWeight = onboardDoc.getString("targetWeight") ?: "-",
                            height = onboardDoc.getString("height") ?: "-",
                            goal = onboardDoc.getString("goal") ?: "-",
                            name = name
                        )
                    }
            }
    }



    fun checkOnboardingCompleted(uid: String) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("onboardingDetails")
            .document("main")
            .get()
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
        onComplete: () -> Unit,
        gender: String,
        dob: String,
        calories: Int,
        protein: Int
    ) {
        _state.value = OnboardingState.Loading

        val data = OnboardingModel(
            currentWeight = currWt,
            targetWeight = targetWt,
            height = height,
            goal = goal,
            gender = gender,
            dob = dob,
            calories = calories,
            protein = protein
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