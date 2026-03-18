package com.example.smartfit.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object HealthCalculator {

    fun calculateAge(dob: String): Int {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val birthDate = sdf.parse(dob) ?: return 0

            val birthCal = Calendar.getInstance()
            birthCal.time = birthDate

            val today = Calendar.getInstance()

            var age = today.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)

            // check if birthday has passed this year
            if (today.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
                age--
            }

            age
        } catch (e: Exception) {
            0
        }
    }

    fun calculateCalories(
        weight: Float,
        height: Float,
        age: Int,
        gender: String,
        goal: String
    ): Int {

        val bmr = if (gender == "Male") {
            10 * weight + 6.25f * height - 5 * age + 5
        } else {
            10 * weight + 6.25f * height - 5 * age - 161
        }

        val tdee = bmr * 1.55f

        return when (goal) {
            "Lose Weight" -> (tdee - 300).toInt()
            "Gain Muscle" -> (tdee + 300).toInt()
            else -> tdee.toInt()
        }
    }

    fun calculateProtein(weight: Float, goal: String): Int {
        val multiplier = when (goal) {
            "Lose Weight" -> 1.8f
            "Gain Muscle" -> 2.2f
            else -> 1.5f
        }
        return (weight * multiplier).toInt()
    }
}