package com.example.smartfit.model

data class WorkoutLog(
    val name: String = "",
    val sets: Int = 0,
    val reps: Int = 0,
    val weight: Float = 0f,
    val calories: Float = 0f,
    val docId: String = ""
)

