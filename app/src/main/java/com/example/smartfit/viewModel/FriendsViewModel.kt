package com.example.smartfit.viewModel

import androidx.lifecycle.ViewModel
import com.example.smartfit.model.FriendModel
import com.example.smartfit.model.League
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SearchResult(
    val uid: String = "",
    val name: String = "",
    val email: String = ""
)

class FriendsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _friends = MutableStateFlow<List<FriendModel>>(emptyList())
    val friends: StateFlow<List<FriendModel>> = _friends

    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _friendUids = MutableStateFlow<Set<String>>(emptySet())
    val friendUids: StateFlow<Set<String>> = _friendUids

    init {
        if (uid.isNotEmpty()) loadFriends()
    }

    fun loadFriends() {
        if (uid.isEmpty()) return

        db.collection("users")
            .document(uid)
            .collection("friends")
            .addSnapshotListener { snap, _ ->
                val friendIds = snap?.documents?.mapNotNull { it.id } ?: emptyList()
                _friendUids.value = friendIds.toSet()

                if (friendIds.isEmpty()) {
                    _friends.value = emptyList()
                    return@addSnapshotListener
                }

                // Fetch each friend's profile and points
                val result = mutableListOf<FriendModel>()
                var completed = 0

                for (fid in friendIds) {
                    fetchFriendData(fid) { friend ->
                        if (friend != null) result.add(friend)
                        completed++
                        if (completed == friendIds.size) {
                            _friends.value = result.sortedByDescending { it.totalPoints }
                        }
                    }
                }
            }
    }

    private fun fetchFriendData(friendUid: String, callback: (FriendModel?) -> Unit) {
        db.collection("users")
            .document(friendUid)
            .collection("profile")
            .document("main")
            .get()
            .addOnSuccessListener { profileDoc ->
                val name = profileDoc.getString("name") ?: "User"
                val email = profileDoc.getString("email") ?: ""

                db.collection("users")
                    .document(friendUid)
                    .collection("points")
                    .document("main")
                    .get()
                    .addOnSuccessListener { pointsDoc ->
                        val pts = pointsDoc.getLong("totalPoints")?.toInt() ?: 0
                        callback(
                            FriendModel(
                                uid = friendUid,
                                name = name,
                                email = email,
                                totalPoints = pts,
                                league = League.fromPoints(pts)
                            )
                        )
                    }
                    .addOnFailureListener {
                        callback(
                            FriendModel(
                                uid = friendUid,
                                name = name,
                                email = email,
                                totalPoints = 0,
                                league = League.BRONZE
                            )
                        )
                    }
            }
            .addOnFailureListener { callback(null) }
    }

    fun searchUsers(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            _isSearching.value = false
            return
        }

        _isSearching.value = true
        val lowerQuery = query.lowercase()

        db.collection("users")
            .get()
            .addOnSuccessListener { usersSnap ->
                val results = mutableListOf<SearchResult>()
                var completed = 0
                val userDocs = usersSnap.documents

                if (userDocs.isEmpty()) {
                    _searchResults.value = emptyList()
                    _isSearching.value = false
                    return@addOnSuccessListener
                }

                for (userDoc in userDocs) {
                    val userId = userDoc.id
                    if (userId == uid) {
                        completed++
                        if (completed == userDocs.size) {
                            _searchResults.value = results
                            _isSearching.value = false
                        }
                        continue
                    }

                    db.collection("users")
                        .document(userId)
                        .collection("profile")
                        .document("main")
                        .get()
                        .addOnSuccessListener { profileDoc ->
                            val name = profileDoc.getString("name") ?: ""
                            val email = profileDoc.getString("email") ?: ""

                            if (name.lowercase().contains(lowerQuery) ||
                                email.lowercase().contains(lowerQuery)
                            ) {
                                results.add(SearchResult(uid = userId, name = name, email = email))
                            }

                            completed++
                            if (completed == userDocs.size) {
                                _searchResults.value = results
                                _isSearching.value = false
                            }
                        }
                        .addOnFailureListener {
                            completed++
                            if (completed == userDocs.size) {
                                _searchResults.value = results
                                _isSearching.value = false
                            }
                        }
                }
            }
            .addOnFailureListener {
                _searchResults.value = emptyList()
                _isSearching.value = false
            }
    }

    fun addFriend(friendUid: String) {
        if (uid.isEmpty() || friendUid == uid) return

        // Add to current user's friends
        db.collection("users")
            .document(uid)
            .collection("friends")
            .document(friendUid)
            .set(mapOf("uid" to friendUid, "addedAt" to System.currentTimeMillis()))

        // Add current user to friend's friends (mutual)
        db.collection("users")
            .document(friendUid)
            .collection("friends")
            .document(uid)
            .set(mapOf("uid" to uid, "addedAt" to System.currentTimeMillis()))
    }

    fun removeFriend(friendUid: String) {
        if (uid.isEmpty()) return

        db.collection("users")
            .document(uid)
            .collection("friends")
            .document(friendUid)
            .delete()

        db.collection("users")
            .document(friendUid)
            .collection("friends")
            .document(uid)
            .delete()
    }
}
