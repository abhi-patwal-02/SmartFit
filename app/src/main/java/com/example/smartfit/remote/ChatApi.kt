package com.example.smartfit.remote

import com.example.smartfit.model.ChatRequestDto
import com.example.smartfit.model.ChatResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {

    @POST("chat")
    suspend fun sendMessage(
        @Body request: ChatRequestDto
    ): ChatResponseDto
}
