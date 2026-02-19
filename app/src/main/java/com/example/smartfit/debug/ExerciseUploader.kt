package com.example.smartfit.debug

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object ExerciseUploader {

    fun uploadExercises() {

        val db = FirebaseFirestore.getInstance()

        val exercises = listOf(

            // CHEST
            mapOf("name" to "Barbell Bench Press","type" to "strength","caloriesPerRep" to 0.45f),
            mapOf("name" to "Incline Barbell Bench Press","type" to "strength","caloriesPerRep" to 0.46f),
            mapOf("name" to "Decline Barbell Bench Press","type" to "strength","caloriesPerRep" to 0.46f),
            mapOf("name" to "Dumbbell Bench Press","type" to "strength","caloriesPerRep" to 0.44f),
            mapOf("name" to "Incline Dumbbell Press","type" to "strength","caloriesPerRep" to 0.44f),
            mapOf("name" to "Decline Dumbbell Press","type" to "strength","caloriesPerRep" to 0.44f),
            mapOf("name" to "Cable Chest Fly","type" to "strength","caloriesPerRep" to 0.36f),
            mapOf("name" to "Chest Fly Machine","type" to "strength","caloriesPerRep" to 0.35f),
            mapOf("name" to "Weighted Push Up","type" to "strength","caloriesPerRep" to 0.42f),

            // BACK
            mapOf("name" to "Deadlift","type" to "strength","caloriesPerRep" to 0.65f),
            mapOf("name" to "Barbell Row","type" to "strength","caloriesPerRep" to 0.50f),
            mapOf("name" to "T-Bar Row","type" to "strength","caloriesPerRep" to 0.52f),
            mapOf("name" to "Seated Cable Row","type" to "strength","caloriesPerRep" to 0.40f),
            mapOf("name" to "Lat Pulldown","type" to "strength","caloriesPerRep" to 0.38f),
            mapOf("name" to "Weighted Pull Up","type" to "strength","caloriesPerRep" to 0.55f),
            mapOf("name" to "Single Arm Dumbbell Row","type" to "strength","caloriesPerRep" to 0.44f),
            mapOf("name" to "Face Pull","type" to "strength","caloriesPerRep" to 0.32f),

            // SHOULDERS
            mapOf("name" to "Barbell Overhead Press","type" to "strength","caloriesPerRep" to 0.48f),
            mapOf("name" to "Dumbbell Shoulder Press","type" to "strength","caloriesPerRep" to 0.46f),
            mapOf("name" to "Arnold Press","type" to "strength","caloriesPerRep" to 0.47f),
            mapOf("name" to "Lateral Raise","type" to "strength","caloriesPerRep" to 0.30f),
            mapOf("name" to "Cable Lateral Raise","type" to "strength","caloriesPerRep" to 0.31f),
            mapOf("name" to "Front Raise (Plate)","type" to "strength","caloriesPerRep" to 0.28f),
            mapOf("name" to "Reverse Pec Deck","type" to "strength","caloriesPerRep" to 0.29f),
            mapOf("name" to "Barbell Shrugs","type" to "strength","caloriesPerRep" to 0.35f),
            mapOf("name" to "Dumbbell Shrugs","type" to "strength","caloriesPerRep" to 0.34f),

            // LEGS
            mapOf("name" to "Barbell Squat","type" to "strength","caloriesPerRep" to 0.55f),
            mapOf("name" to "Front Squat","type" to "strength","caloriesPerRep" to 0.55f),
            mapOf("name" to "Smith Machine Squat","type" to "strength","caloriesPerRep" to 0.53f),
            mapOf("name" to "Hack Squat Machine","type" to "strength","caloriesPerRep" to 0.52f),
            mapOf("name" to "Leg Press","type" to "strength","caloriesPerRep" to 0.50f),
            mapOf("name" to "Goblet Squat","type" to "strength","caloriesPerRep" to 0.48f),
            mapOf("name" to "Bulgarian Split Squat","type" to "strength","caloriesPerRep" to 0.52f),
            mapOf("name" to "Walking Lunges (Weighted)","type" to "strength","caloriesPerRep" to 0.50f),
            mapOf("name" to "Dumbbell Lunges","type" to "strength","caloriesPerRep" to 0.48f),
            mapOf("name" to "Leg Extension Machine","type" to "strength","caloriesPerRep" to 0.30f),
            mapOf("name" to "Seated Leg Curl","type" to "strength","caloriesPerRep" to 0.32f),
            mapOf("name" to "Lying Leg Curl","type" to "strength","caloriesPerRep" to 0.32f),
            mapOf("name" to "Standing Calf Raise (Machine)","type" to "strength","caloriesPerRep" to 0.25f),
            mapOf("name" to "Seated Calf Raise","type" to "strength","caloriesPerRep" to 0.25f),

            // ARMS
            mapOf("name" to "Barbell Curl","type" to "strength","caloriesPerRep" to 0.30f),
            mapOf("name" to "Dumbbell Curl","type" to "strength","caloriesPerRep" to 0.28f),
            mapOf("name" to "Hammer Curl","type" to "strength","caloriesPerRep" to 0.29f),
            mapOf("name" to "Cable Curl","type" to "strength","caloriesPerRep" to 0.27f),
            mapOf("name" to "Preacher Curl Machine","type" to "strength","caloriesPerRep" to 0.28f),
            mapOf("name" to "Skull Crushers","type" to "strength","caloriesPerRep" to 0.33f),
            mapOf("name" to "Cable Tricep Pushdown","type" to "strength","caloriesPerRep" to 0.30f),
            mapOf("name" to "Overhead Dumbbell Extension","type" to "strength","caloriesPerRep" to 0.32f),
            mapOf("name" to "Weighted Dips","type" to "strength","caloriesPerRep" to 0.40f)
        )

        exercises.forEach { ex ->
            db.collection("exercises")
                .add(ex)
                .addOnSuccessListener {
                    Log.d("EX_UPLOAD", "Added: ${ex["name"]}")
                }
                .addOnFailureListener {
                    Log.e("EX_UPLOAD", "Failed: ${ex["name"]}")
                }
        }
    }
}
