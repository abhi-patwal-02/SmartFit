package com.example.smartfit.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smartfit.model.FoodItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Represents a single item in the Recent Activity feed
data class ActivityItem(
    val label: String = "",    // e.g. "Push-ups" or "Oatmeal"
    val detail: String = "",   // e.g. "3×12 reps" or "350 kcal"
    val type: ActivityType = ActivityType.WORKOUT
)

enum class ActivityType { WORKOUT, MEAL }

// One bar in the weekly chart: a day label + calorie value
data class WeekDaySummary(
    val dayLabel: String,       // e.g. "Mon"
    val caloriesBurned: Float,
    val caloriesConsumed: Float
)

class DashboardViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // ── Existing: today's nutrition targets & consumed ──────────────────────
    var caloriesTarget by mutableStateOf(0)
        private set

    var proteinTarget by mutableStateOf(0)
        private set

    var caloriesConsumed by mutableStateOf(0)
        private set

    var proteinConsumed by mutableStateOf(0)
        private set

    // ── Feature 3: Burned vs Consumed balance ────────────────────────────────
    var caloriesBurnedToday by mutableStateOf(0f)
        private set

    // ── Feature 1: Recent Activity feed ─────────────────────────────────────
    private val _recentActivity = MutableStateFlow<List<ActivityItem>>(emptyList())
    val recentActivity: StateFlow<List<ActivityItem>> = _recentActivity

    // ── Feature 2: Weekly calorie chart ─────────────────────────────────────
    private val _weeklyData = MutableStateFlow<List<WeekDaySummary>>(emptyList())
    val weeklyData: StateFlow<List<WeekDaySummary>> = _weeklyData

    init {
        if (uid.isNotEmpty()) {
            loadTargets()
            loadTodayBurnedCalories()
            loadRecentWorkouts()
            loadRecentMeals()
            loadWeeklySummary()
        }
    }

    // ── Targets & today's nutrition ──────────────────────────────────────────
    fun loadTargets() {
        if (uid.isEmpty()) return
        db.collection("users")
            .document(uid)
            .collection("onboardingDetails")
            .document("main")
            .addSnapshotListener { snap, _ ->
                snap?.let {
                    caloriesTarget = it.getLong("calories")?.toInt() ?: 0
                    proteinTarget  = it.getLong("protein")?.toInt()  ?: 0
                }
            }
    }

    fun updateTotals(foods: List<FoodItem>) {
        caloriesConsumed = foods.sumOf { it.calories }
        proteinConsumed  = foods.sumOf { it.protein }
    }

    // ── Feature 3: Today's burned calories ───────────────────────────────────
    private fun loadTodayBurnedCalories() {
        if (uid.isEmpty()) return
        val todayStr = today()
        db.collection("users")
            .document(uid)
            .collection("dailySummary")
            .document(todayStr)
            .addSnapshotListener { snap, _ ->
                caloriesBurnedToday = snap?.getDouble("burnedCalories")?.toFloat() ?: 0f
            }
    }

    // ── Feature 1: Recent workouts (today) ───────────────────────────────────
    private val _recentWorkouts = MutableStateFlow<List<ActivityItem>>(emptyList())
    private val _recentMeals   = MutableStateFlow<List<ActivityItem>>(emptyList())

    private fun loadRecentWorkouts() {
        if (uid.isEmpty()) return
        db.collection("users")
            .document(uid)
            .collection("workouts")
            .document(today())
            .collection("items")
            .addSnapshotListener { snap, _ ->
                val items = snap?.documents?.mapNotNull { doc ->
                    val name = doc.getString("name") ?: return@mapNotNull null
                    val sets = doc.getLong("sets")?.toInt() ?: 0
                    val reps = doc.getLong("reps")?.toInt() ?: 0
                    val cal  = doc.getDouble("calories")?.toInt() ?: 0
                    ActivityItem(
                        label  = name,
                        detail = if (reps > 0) "${sets}×${reps} reps · ${cal} kcal" else "${sets} min · ${cal} kcal",
                        type   = ActivityType.WORKOUT
                    )
                } ?: emptyList()
                _recentWorkouts.value = items
                mergeActivity()
            }
    }

    private fun loadRecentMeals() {
        if (uid.isEmpty()) return
        db.collection("users")
            .document(uid)
            .collection("nutrition")
            .document(today())
            .collection("meals")
            .addSnapshotListener { snap, _ ->
                val items = snap?.documents?.mapNotNull { doc ->
                    val name = doc.getString("name") ?: return@mapNotNull null
                    val cal  = doc.getLong("calories")?.toInt() ?: 0
                    ActivityItem(
                        label  = name,
                        detail = "$cal kcal",
                        type   = ActivityType.MEAL
                    )
                } ?: emptyList()
                _recentMeals.value = items
                mergeActivity()
            }
    }

    private fun mergeActivity() {
        // Interleave workouts and meals, cap at 6 most recent items
        val combined = (_recentWorkouts.value + _recentMeals.value).take(6)
        _recentActivity.value = combined
    }

    // ── Feature 2: 7-day summary ─────────────────────────────────────────────
    private fun loadWeeklySummary() {
        if (uid.isEmpty()) return
        val sdf     = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dayFmt  = SimpleDateFormat("EEE", Locale.getDefault())
        val cal     = Calendar.getInstance()
        val results = mutableListOf<WeekDaySummary>()
        var fetched = 0

        // Build list of last 7 date strings (oldest first)
        val dates = (6 downTo 0).map { offset ->
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_YEAR, -offset)
            Pair(sdf.format(c.time), dayFmt.format(c.time))
        }

        // Pre-fill with zeroes so chart shows all 7 bars even if no data
        val placeholders = dates.map { (_, label) ->
            WeekDaySummary(dayLabel = label, caloriesBurned = 0f, caloriesConsumed = 0f)
        }.toMutableList()
        _weeklyData.value = placeholders

        dates.forEachIndexed { index, (dateStr, dayLabel) ->
            db.collection("users")
                .document(uid)
                .collection("dailySummary")
                .document(dateStr)
                .get()
                .addOnSuccessListener { snap ->
                    val burned   = snap?.getDouble("burnedCalories")?.toFloat()   ?: 0f
                    val consumed = snap?.getDouble("caloriesConsumed")?.toFloat() ?: 0f
                    placeholders[index] = WeekDaySummary(
                        dayLabel        = dayLabel,
                        caloriesBurned  = burned,
                        caloriesConsumed = consumed
                    )
                    fetched++
                    if (fetched == 7) {
                        _weeklyData.value = placeholders.toList()
                    }
                }
                .addOnFailureListener {
                    fetched++
                    if (fetched == 7) {
                        _weeklyData.value = placeholders.toList()
                    }
                }
        }
    }

    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}