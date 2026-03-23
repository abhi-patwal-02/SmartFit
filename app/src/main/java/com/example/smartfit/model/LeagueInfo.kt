package com.example.smartfit.model

import androidx.compose.ui.graphics.Color

enum class League(
    val minPoints: Int,
    val displayName: String,
    val color: Color
) {
    BRONZE(0, "Bronze", Color(0xFFCD7F32)),
    SILVER(100, "Silver", Color(0xFFC0C0C0)),
    GOLD(300, "Gold", Color(0xFFFFD700)),
    DIAMOND(600, "Diamond", Color(0xFFB9F2FF)),
    LEGEND(1000, "Legend", Color(0xFFFF7043));

    companion object {
        fun fromPoints(points: Int): League {
            return entries.sortedByDescending { it.minPoints }
                .firstOrNull { points >= it.minPoints } ?: BRONZE
        }

        fun nextLeague(current: League): League? {
            val idx = entries.indexOf(current)
            return if (idx < entries.size - 1) entries[idx + 1] else null
        }
    }
}
