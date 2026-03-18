package com.example.smartfit.model

data class OnboardingModel(
    val currentWeight: String = "",
    val targetWeight: String = "",
    val height: String = "",
    val goal: String = "",
    val gender: String = "",
    val dob: String = "",
    val calories: Int,
    val protein: Int
){
    fun toMap(): Map<String, Any> = mapOf(
        "currentWeight" to currentWeight,
        "targetWeight" to targetWeight,
        "height" to height,
        "goal" to goal,
        "geneder" to gender,
        "dob" to dob,
        "calories" to calories,
        "protein" to protein
    )
}
