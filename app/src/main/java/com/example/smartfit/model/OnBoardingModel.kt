package com.example.smartfit.model

data class OnboardingModel(
    val currentWeight: String = "",
    val targetWeight: String = "",
    val height: String = "",
    val goal: String = ""
){
    fun toMap(): Map<String, Any> = mapOf(
        "currentWeight" to currentWeight,
        "targetWeight" to targetWeight,
        "height" to height,
        "goal" to goal
    )
}
