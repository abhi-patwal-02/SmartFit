package com.example.smartfit.model

data class ChatRequestDto(
    val message: String,
    val user_id: String
)

data class ChatResponseDto(
    val response: String
)
