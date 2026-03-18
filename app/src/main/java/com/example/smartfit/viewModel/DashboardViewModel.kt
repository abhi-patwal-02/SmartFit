package com.example.smartfit.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smartfit.model.FoodItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    var caloriesTarget by mutableStateOf(0)
        private set

    var proteinTarget by mutableStateOf(0)
        private set

    var caloriesConsumed by mutableStateOf(0)
        private set

    var proteinConsumed by mutableStateOf(0)
        private set

    fun loadTargets() {

        db.collection("users")
            .document(uid)
            .collection("onboardingDetails")
            .document("main")
            .addSnapshotListener { snap, _ ->

                snap?.let {
                    caloriesTarget = it.getLong("calories")?.toInt() ?: 0
                    proteinTarget = it.getLong("protein")?.toInt() ?: 0
                }
            }
    }

    // 🔹 Called from UI when foods change
    fun updateTotals(foods: List<FoodItem>) {

        caloriesConsumed = foods.sumOf { it.calories }
        proteinConsumed = foods.sumOf { it.protein }
    }

    fun listenToTodayFood() {

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        db.collection("users")
            .document(uid)
            .collection("nutrition")
            .document(today)
            .collection("meals")
            .addSnapshotListener { snap, _ ->

                val foods = snap?.documents?.mapNotNull {
                    it.toObject(FoodItem::class.java)
                } ?: emptyList()

                caloriesConsumed = foods.sumOf { it.calories }
                proteinConsumed = foods.sumOf { it.protein }
            }
    }
}