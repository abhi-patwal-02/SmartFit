package com.example.smartfit.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.smartfit.model.FoodItem
import com.example.smartfit.util.today
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NutritionViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _foods = MutableStateFlow<List<FoodItem>>(emptyList())
    val foods: StateFlow<List<FoodItem>> = _foods

    init {
        listenToTodayFoods()
    }

    // 🔹 FETCH REALTIME
    private fun listenToTodayFoods() {

        db.collection("users")
            .document(uid)
            .collection("nutrition")
            .document(today())
            .collection("meals")
            .addSnapshotListener { snapshot, _ ->

                _foods.value = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(FoodItem::class.java)?.copy(docId = doc.id)
                } ?: emptyList()

            }
    }

    // 🔹 ADD FOOD
    fun addFood(food: FoodItem) {

        db.collection("users")
            .document(uid)
            .collection("nutrition")
            .document(today())
            .collection("meals")
            .add(food)
    }

    // 🔹 DELETE FOOD
    fun deleteFood(docId: String) {

        db.collection("users")
            .document(uid)
            .collection("nutrition")
            .document(today())
            .collection("meals")
            .document(docId)
            .delete()
    }

}
