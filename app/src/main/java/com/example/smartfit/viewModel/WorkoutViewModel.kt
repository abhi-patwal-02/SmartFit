package com.example.smartfit.viewModel

import androidx.lifecycle.ViewModel
import com.example.smartfit.model.ExerciseItem
import com.example.smartfit.model.FoodItem
import com.example.smartfit.model.WorkoutLog
import com.example.smartfit.util.today
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.firebase.firestore.SetOptions


class WorkoutViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _workouts = MutableStateFlow<List<WorkoutLog>>(emptyList())
    val workouts: StateFlow<List<WorkoutLog>> = _workouts

    init { listenToTodayWorkouts() }

    private fun listenToTodayWorkouts() {
        val uidVal = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users")
            .document(uidVal)
            .collection("workouts")
            .document(today())
            .collection("items")
            .addSnapshotListener { snap, _ ->
                _workouts.value = snap?.documents?.mapNotNull {
                    it.toObject(WorkoutLog::class.java)?.copy(docId = it.id)
                } ?: emptyList()
            }
    }

    fun addWorkout(ex: ExerciseItem, sets: Int, reps: Int, weight: Float) {

        val uidVal = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val calories =
            if (ex.type == "strength")
                ex.caloriesPerRep * reps * sets
            else
                ex.caloriesPerMin * sets   // treat sets as minutes for cardio

        val log = WorkoutLog(
            name = ex.name,
            sets = sets,
            reps = reps,
            weight = weight,
            calories = calories
        )

        db.collection("users")
            .document(uidVal)
            .collection("workouts")
            .document(today())
            .collection("items")
            .add(log)

        updateBurnedCalories(calories)

        // Award points for completing a workout
        PointsViewModel.awardPointsStatic()
    }

    private fun updateBurnedCalories(cal: Float) {

        val uidVal = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val ref = db.collection("users")
            .document(uidVal)
            .collection("dailySummary")
            .document(today())

        db.runTransaction { t ->
            val snap = t.get(ref)
            val burned = snap.getDouble("burnedCalories") ?: 0.0
            t.set(ref, mapOf("burnedCalories" to burned + cal), SetOptions.merge())
        }
    }
}
