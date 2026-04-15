package com.example.smartfit.debug

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * HistorySeeder2 — Debug utility to seed 4 more days of realistic workout + meal history
 * for the currently logged-in user into Firestore.
 */
object HistorySeeder2 {

    private val db = FirebaseFirestore.getInstance()
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private const val TAG = "HistorySeeder2"

    // ── Meal templates — drawn from FoodUploader food list ──────────────────────
    private val breakfasts = listOf(
        listOf(
            meal("Oats", 150, 5, 27, 3),
            meal("Boiled Egg", 78, 6, 1, 5),
            meal("Milk", 120, 8, 12, 5)
        ),
        listOf(
            meal("Bread", 160, 6, 30, 2),
            meal("Egg White", 68, 16, 0, 0),
            meal("Tea", 40, 1, 6, 1)
        ),
        listOf(
            meal("Idli", 174, 6, 36, 0),
            meal("Curd", 98, 5, 7, 4)
        ),
        listOf(
            meal("Dosa", 168, 4, 27, 4),
            meal("Tea", 40, 1, 6, 1)
        ),
        listOf(
            meal("Paratha", 260, 5, 30, 14),
            meal("Curd", 98, 5, 7, 4)
        )
    )

    private val lunches = listOf(
        listOf(
            meal("Chicken Breast", 165, 31, 0, 4),
            meal("Brown Rice", 216, 5, 44, 2),
            meal("Dal Tadka", 180, 9, 22, 6)
        ),
        listOf(
            meal("Rajma", 220, 11, 33, 4),
            meal("White Rice", 205, 4, 45, 0),
            meal("Roti", 120, 3, 18, 3)
        ),
        listOf(
            meal("Biryani", 450, 20, 55, 18)
        ),
        listOf(
            meal("Grilled Fish", 190, 28, 0, 9),
            meal("Brown Rice", 216, 5, 44, 2),
            meal("Dal Tadka", 180, 9, 22, 6)
        ),
        listOf(
            meal("Chole", 240, 12, 35, 6),
            meal("Roti", 240, 6, 36, 6)
        )
    )

    private val dinners = listOf(
        listOf(
            meal("Grilled Chicken", 200, 32, 0, 6),
            meal("Khichdi", 210, 8, 30, 5)
        ),
        listOf(
            meal("Paneer", 265, 18, 6, 21),
            meal("Roti", 240, 6, 36, 6)
        ),
        listOf(
            meal("Fish Curry", 210, 22, 5, 12),
            meal("White Rice", 205, 4, 45, 0)
        ),
        listOf(
            meal("Tofu", 144, 15, 4, 9),
            meal("Brown Rice", 216, 5, 44, 2)
        ),
        listOf(
            meal("Dal Tadka", 180, 9, 22, 6),
            meal("Roti", 360, 9, 54, 9),
            meal("Curd", 98, 5, 7, 4)
        )
    )

    private val snacks = listOf(
        listOf(meal("Almonds", 164, 6, 6, 14)),
        listOf(meal("Protein Bar", 220, 20, 23, 7)),
        listOf(meal("Banana", 105, 1, 27, 0)),
        listOf(meal("Peanuts", 170, 7, 6, 14)),
        listOf(meal("Greek Yogurt", 130, 10, 4, 7))
    )

    // ── Workout templates ────────────────────────────────────────────────────────
    private val workoutDays = listOf(
        // Push day
        listOf(
            workout("Barbell Bench Press", 4, 8, 80f, 0.45f),
            workout("Incline Dumbbell Press", 3, 10, 30f, 0.44f),
            workout("Cable Chest Fly", 3, 12, 20f, 0.36f),
            workout("Dumbbell Shoulder Press", 3, 10, 22f, 0.46f),
            workout("Lateral Raise", 3, 15, 10f, 0.30f),
            workout("Cable Tricep Pushdown", 3, 12, 25f, 0.30f)
        ),
        // Pull day
        listOf(
            workout("Deadlift", 4, 5, 100f, 0.65f),
            workout("Barbell Row", 4, 8, 70f, 0.50f),
            workout("Lat Pulldown", 3, 10, 60f, 0.38f),
            workout("Seated Cable Row", 3, 12, 55f, 0.40f),
            workout("Face Pull", 3, 15, 20f, 0.32f),
            workout("Barbell Curl", 3, 12, 30f, 0.30f)
        ),
        // Legs day
        listOf(
            workout("Barbell Squat", 4, 6, 100f, 0.55f),
            workout("Leg Press", 3, 10, 120f, 0.50f),
            workout("Bulgarian Split Squat", 3, 8, 20f, 0.52f),
            workout("Leg Extension Machine", 3, 12, 50f, 0.30f),
            workout("Seated Leg Curl", 3, 12, 45f, 0.32f),
            workout("Standing Calf Raise (Machine)", 4, 15, 60f, 0.25f)
        ),
        // Shoulders + Arms
        listOf(
            workout("Barbell Overhead Press", 4, 8, 60f, 0.48f),
            workout("Arnold Press", 3, 10, 18f, 0.47f),
            workout("Lateral Raise", 4, 15, 10f, 0.30f),
            workout("Dumbbell Curl", 3, 12, 16f, 0.28f),
            workout("Skull Crushers", 3, 12, 25f, 0.33f),
            workout("Weighted Dips", 3, 10, 20f, 0.40f)
        ),
        // Upper body light/cardio-style
        listOf(
            workout("Weighted Push Up", 4, 15, 10f, 0.42f),
            workout("Weighted Pull Up", 4, 8, 10f, 0.55f),
            workout("Dumbbell Shoulder Press", 3, 12, 18f, 0.46f),
            workout("Hammer Curl", 3, 12, 14f, 0.29f),
            workout("Cable Tricep Pushdown", 3, 15, 20f, 0.30f)
        )
    )

    // ── Public entry point ───────────────────────────────────────────────────────
    fun seed() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            Log.e(TAG, "No user logged in — aborting seed")
            return
        }
        Log.d(TAG, "Seeding 4 extra days of history for uid=\$uid")
        seedForUser(uid)
    }

    private fun seedForUser(uid: String) {
        val cal = Calendar.getInstance()
        var totalPoints = 0
        var streak = 0
        var lastWorkoutDate = ""

        val rng = java.util.Random(uid.hashCode().toLong() + 1L) // slightly different seed
        val shuffledBreakfasts = breakfasts.shuffled(rng)
        val shuffledLunches    = lunches.shuffled(rng)
        val shuffledSnacks     = snacks.shuffled(rng)
        val shuffledDinners    = dinners.shuffled(rng)
        val shuffledWorkouts   = workoutDays.shuffled(rng)

        // Seed for recent 4 days: 4 days ago to 1 day ago
        for (daysAgo in 4 downTo 1) {
            cal.time = java.util.Date()
            cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
            val dateStr = sdf.format(cal.time)

            val dayIndex = (4 - daysAgo) % shuffledWorkouts.size
            val mealIdx  = (4 - daysAgo) % shuffledBreakfasts.size

            // ── Write meals ──────────────────────────────────────────────────────
            val dayMeals = shuffledBreakfasts[mealIdx] + shuffledLunches[mealIdx] +
                           shuffledSnacks[mealIdx] + shuffledDinners[mealIdx]
            
            var totalConsumed = 0f
            dayMeals.forEach { m ->
                totalConsumed += (m["calories"] as Int).toFloat()
                db.collection("users").document(uid)
                    .collection("nutrition").document(dateStr)
                    .collection("meals")
                    .add(m)
                    .addOnFailureListener { Log.e(TAG, "Meal write fail [\$dateStr]: \${it.message}") }
            }

            // ── Write workouts & daily burned calories ───────────────────────────
            val dayWorkouts = shuffledWorkouts[dayIndex]
            var totalBurned = 0f
            dayWorkouts.forEach { w ->
                db.collection("users").document(uid)
                    .collection("workouts").document(dateStr)
                    .collection("items")
                    .add(w)
                    .addOnFailureListener { Log.e(TAG, "Workout write fail [\$dateStr]: \${it.message}") }
                totalBurned += w["calories"] as Float
            }

            db.collection("users").document(uid)
                .collection("dailySummary").document(dateStr)
                .set(
                    mapOf(
                        "burnedCalories" to totalBurned,
                        "caloriesConsumed" to totalConsumed
                    ), 
                    SetOptions.merge()
                )

            // ── Compute points for this day (same rules as PointsViewModel) ──────
            val isConsecutive = lastWorkoutDate == getPreviousDate(dateStr)
            if (isConsecutive) {
                streak++
                val bonus = if (streak == 2) 5 else 10
                totalPoints += 10 + bonus
            } else {
                streak = 1
                totalPoints += 10
            }
            lastWorkoutDate = dateStr

            Log.d(TAG, "Seeded \$dateStr | streak=\$streak | totalPoints=\$totalPoints")
        }

        // ── Write final accumulated points ───────────────────────────────────────
        val pointsData = mapOf(
            "totalPoints"       to totalPoints,
            "currentStreak"     to streak,
            "lastWorkoutDate"   to lastWorkoutDate
        )
        db.collection("users").document(uid)
            .collection("points").document("main")
            .set(pointsData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "✅ Seed 2 complete! totalPoints=\$totalPoints streak=\$streak lastDate=\$lastWorkoutDate")
            }
            .addOnFailureListener {
                Log.e(TAG, "Points write failed: \${it.message}")
            }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────────

    private fun getPreviousDate(dateStr: String): String {
        val cal = Calendar.getInstance()
        cal.time = sdf.parse(dateStr)!!
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return sdf.format(cal.time)
    }

    private fun meal(
        name: String, calories: Int, protein: Int, carbs: Int, fat: Int
    ): Map<String, Any> = mapOf(
        "name"     to name,
        "calories" to calories,
        "protein"  to protein,
        "carbs"    to carbs,
        "fat"      to fat
    )

    private fun workout(
        name: String, sets: Int, reps: Int, weight: Float, caloriesPerRep: Float
    ): Map<String, Any> {
        val calories = caloriesPerRep * reps * sets
        return mapOf(
            "name"     to name,
            "sets"     to sets,
            "reps"     to reps,
            "weight"   to weight,
            "calories" to calories
        )
    }
}
