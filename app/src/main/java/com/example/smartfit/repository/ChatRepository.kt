package com.example.smartfit.repository

import com.example.smartfit.model.ChatRequestDto
import com.example.smartfit.remote.RetrofitClient

class ChatRepository {

    suspend fun sendMessage(message: String): String {
        return try {
            val response = RetrofitClient.api.sendMessage(
                ChatRequestDto(message)
            )
            response.response
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}
