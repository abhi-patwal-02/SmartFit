package com.example.smartfit.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.smartfit.model.FoodItem
import com.example.smartfit.util.today
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NutritionViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val uid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _foods = MutableStateFlow<List<FoodItem>>(emptyList())
    val foods: StateFlow<List<FoodItem>> = _foods

    init {
        listenToTodayFoods()
    }

    // 🔹 FETCH REALTIME — listens to all meal documents for today
    private fun listenToTodayFoods() {
        if (uid.isEmpty()) return

        db.collection("users")
            .document(uid)
            .collection("nutrition")
            .document(today())
            .collection("meals")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("NutritionVM", "Listen failed: ${error.message}")
                    return@addSnapshotListener
                }
                val currentFoods = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(FoodItem::class.java)?.copy(docId = doc.id)
                } ?: emptyList()
                
                _foods.value = currentFoods
                updateDailySummary(currentFoods)
            }
    }

    private fun updateDailySummary(foodsList: List<FoodItem>) {
        if (uid.isEmpty()) return
        val totalCalories = foodsList.sumOf { it.calories }
        
        db.collection("users")
            .document(uid)
            .collection("dailySummary")
            .document(today())
            .set(mapOf("caloriesConsumed" to totalCalories), SetOptions.merge())
    }

    // 🔹 ADD FOOD — writes as explicit map so docId is never stored in Firestore
    fun addFood(food: FoodItem) {
        if (uid.isEmpty()) {
            Log.e("NutritionVM", "Cannot add food: user not logged in")
            return
        }

        val mealData = mapOf(
            "name"     to food.name,
            "calories" to food.calories,
            "protein"  to food.protein,
            "carbs"    to food.carbs,
            "fat"      to food.fat
        )

        db.collection("users")
            .document(uid)
            .collection("nutrition")
            .document(today())
            .collection("meals")
            .add(mealData)
            .addOnSuccessListener { ref ->
                Log.d("NutritionVM", "Meal added: ${ref.id}")
            }
            .addOnFailureListener { e ->
                Log.e("NutritionVM", "Failed to add meal: ${e.message}")
            }
    }

    // 🔹 DELETE FOOD
    fun deleteFood(docId: String) {
        if (uid.isEmpty() || docId.isBlank()) return

        db.collection("users")
            .document(uid)
            .collection("nutrition")
            .document(today())
            .collection("meals")
            .document(docId)
            .delete()
            .addOnFailureListener { e ->
                Log.e("NutritionVM", "Failed to delete meal: ${e.message}")
            }
    }

}
