package com.example.smartfit.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfit.model.ExerciseItem
import com.example.smartfit.model.FoodItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExerciseSearchViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    val query = MutableStateFlow("")
    val results = MutableStateFlow<List<ExerciseItem>>(emptyList())

    init {
        loadExercises()
    }

    private fun loadExercises() {
        db.collection("exercises")
            .get()
            .addOnSuccessListener { snap ->
                val all = snap.toObjects(ExerciseItem::class.java)

                viewModelScope.launch {
                    query.collect { q ->
                        results.value =
                            if (q.isBlank()) emptyList()
                            else all.filter {
                                it.name.contains(q, ignoreCase = true)
                            }
                    }
                }
            }
    }

    fun onQueryChange(q: String) {
        query.value = q
    }
}
