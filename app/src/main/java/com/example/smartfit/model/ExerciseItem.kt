package com.example.smartfit.model

data class ExerciseItem(
    val name: String = "",
    val caloriesPerRep: Float = 0f,
    val caloriesPerMin: Float = 0f,
    val type: String = "",   // strength/cardio
    val docId: String = ""
)

