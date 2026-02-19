package com.example.smartfit.viewModel

import androidx.lifecycle.ViewModel
import com.example.smartfit.model.FoodItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FoodSearchViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _results = MutableStateFlow<List<FoodItem>>(emptyList())
    val results: StateFlow<List<FoodItem>> = _results

    fun onQueryChange(text: String) {
        _query.value = text

        if (text.isBlank()) {
            _results.value = emptyList()
            return
        }

        // fetch foods from Firestore
        db.collection("foods")
            .get()
            .addOnSuccessListener { snapshot ->
                _results.value = snapshot.documents
                    .mapNotNull { it.toObject(FoodItem::class.java) }
                    .filter { it.name.contains(text, ignoreCase = true) }
            }
    }
}
