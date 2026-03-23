package com.example.smartfit.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.smartfit.model.ChatMessage
import com.example.smartfit.repository.ChatRepository

class ChatViewModel : ViewModel() {

    private val repo = ChatRepository()

    var messages = mutableStateListOf<ChatMessage>()
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun sendMessage(text: String, user_id: String) {
        if (text.isBlank()) return

        // Add user message instantly
        messages.add(ChatMessage(text, true))

        viewModelScope.launch {
            isLoading = true

            val reply = repo.sendMessage(text, user_id)

            messages.add(ChatMessage(reply, false))
            isLoading = false
        }
    }
}
