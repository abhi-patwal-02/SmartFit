package com.example.smartfit.viewModel

import androidx.lifecycle.ViewModel
import com.example.smartfit.model.League
import com.example.smartfit.model.UserPoints
import com.example.smartfit.util.today
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PointsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _userPoints = MutableStateFlow(UserPoints())
    val userPoints: StateFlow<UserPoints> = _userPoints

    private val _league = MutableStateFlow(League.BRONZE)
    val league: StateFlow<League> = _league

    init {
        if (uid.isNotEmpty()) listenToPoints()
    }

    private fun listenToPoints() {
        db.collection("users")
            .document(uid)
            .collection("points")
            .document("main")
            .addSnapshotListener { snap, _ ->
                snap?.let {
                    val pts = UserPoints(
                        totalPoints = it.getLong("totalPoints")?.toInt() ?: 0,
                        currentStreak = it.getLong("currentStreak")?.toInt() ?: 0,
                        lastWorkoutDate = it.getString("lastWorkoutDate") ?: ""
                    )
                    _userPoints.value = pts
                    _league.value = League.fromPoints(pts.totalPoints)
                }
            }
    }

    /**
     * Call after a workout is logged. Calculates streak and awards points.
     * Base: +10 per workout
     * Streak day 2: +5 bonus
     * Streak day 3+: +10 bonus
     */
    fun awardPoints() {
        if (uid.isEmpty()) return

        val ref = db.collection("users")
            .document(uid)
            .collection("points")
            .document("main")

        db.runTransaction { transaction ->
            val snap = transaction.get(ref)

            val currentTotal = snap.getLong("totalPoints")?.toInt() ?: 0
            val currentStreak = snap.getLong("currentStreak")?.toInt() ?: 0
            val lastDate = snap.getString("lastWorkoutDate") ?: ""
            val todayStr = today()

            // Don't double-award for the same day's streak
            val newStreak: Int
            val streakBonus: Int

            if (lastDate == todayStr) {
                // Already worked out today — still give base points but no streak update
                newStreak = currentStreak
                streakBonus = 0
            } else if (lastDate == yesterday()) {
                // Consecutive day
                newStreak = currentStreak + 1
                streakBonus = if (newStreak == 2) 5 else 10
            } else {
                // Streak broken — start fresh
                newStreak = 1
                streakBonus = 0
            }

            val basePoints = 10
            val totalAwarded = basePoints + streakBonus

            val data = mapOf(
                "totalPoints" to currentTotal + totalAwarded,
                "currentStreak" to newStreak,
                "lastWorkoutDate" to todayStr
            )

            transaction.set(ref, data, SetOptions.merge())
        }
    }

    private fun yesterday(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return sdf.format(cal.time)
    }

    companion object {
        /**
         * Static helper to award points without needing a ViewModel instance.
         * For use from WorkoutViewModel.
         */
        fun awardPointsStatic() {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
            val db = FirebaseFirestore.getInstance()
            val todayStr = today()

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -1)
            val yesterdayStr = sdf.format(cal.time)

            val ref = db.collection("users")
                .document(uid)
                .collection("points")
                .document("main")

            db.runTransaction { transaction ->
                val snap = transaction.get(ref)

                val currentTotal = snap.getLong("totalPoints")?.toInt() ?: 0
                val currentStreak = snap.getLong("currentStreak")?.toInt() ?: 0
                val lastDate = snap.getString("lastWorkoutDate") ?: ""

                val newStreak: Int
                val streakBonus: Int

                if (lastDate == todayStr) {
                    newStreak = currentStreak
                    streakBonus = 0
                } else if (lastDate == yesterdayStr) {
                    newStreak = currentStreak + 1
                    streakBonus = if (newStreak == 2) 5 else 10
                } else {
                    newStreak = 1
                    streakBonus = 0
                }

                val basePoints = 10
                val totalAwarded = basePoints + streakBonus

                val data = mapOf(
                    "totalPoints" to currentTotal + totalAwarded,
                    "currentStreak" to newStreak,
                    "lastWorkoutDate" to todayStr
                )

                transaction.set(ref, data, SetOptions.merge())
            }
        }
    }
}
