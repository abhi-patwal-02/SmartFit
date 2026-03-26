package com.example.smartfit.model

import com.google.firebase.firestore.Exclude

data class FoodItem(
    val name: String = "",
    val calories: Int = 0,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fat: Int = 0,
    @get:Exclude val docId: String = ""
)

