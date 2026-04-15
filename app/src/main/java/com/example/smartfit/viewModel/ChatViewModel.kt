package com.example.smartfit.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.compose.runtime.*
import com.example.smartfit.model.ChatMessage
import com.example.smartfit.repository.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChatViewModel : ViewModel() {

    private val repo = ChatRepository()
    private val db = FirebaseFirestore.getInstance()
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

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

            var finalPayload = text

            // Check for @WeeklySummary case-insensitively
            if (text.contains("@WeeklySummary", ignoreCase = true)) {
                val summaryData = fetchWeeklySummaryText(user_id)
                finalPayload += "\n\n$summaryData\n\n[System Note: Please give the answer according to the user's data provided above.]"
            }

            val reply = repo.sendMessage(finalPayload, user_id)

            Log.d("CHAT_DEBUG", "chat = $finalPayload")
            Log.d("CHAT_DEBUG", "user_id = $user_id")

            messages.add(ChatMessage(reply, false))
            isLoading = false
        }
    }

    private suspend fun fetchWeeklySummaryText(uid: String): String {
        if (uid.isEmpty()) return "User ID not found."
        val builder = java.lang.StringBuilder("Here is my fitness data for the last 7 days:\n")

        for (offset in 6 downTo 0) {
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_YEAR, -offset)
            val dateStr = sdf.format(c.time)
            
            try {
                val snap = db.collection("users").document(uid)
                    .collection("dailySummary").document(dateStr)
                    .get().await()
                
                val burned = snap.getDouble("burnedCalories")?.toInt() ?: 0
                val consumed = snap.getDouble("caloriesConsumed")?.toInt() ?: 0
                
                builder.append("- $dateStr: $consumed kcal consumed, $burned kcal burned.\n")
            } catch (e: Exception) {
                // Ignore failure for individual day, proceed to next
            }
        }
        return builder.toString()
    }
}
