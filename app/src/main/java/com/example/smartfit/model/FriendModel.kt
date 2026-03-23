package com.example.smartfit.model

data class FriendModel(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val totalPoints: Int = 0,
    val league: League = League.BRONZE
)
