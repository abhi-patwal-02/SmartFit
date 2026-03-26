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

data class FriendRequest(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val timestamp: Long = 0L
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

    private val _receivedRequests = MutableStateFlow<List<FriendRequest>>(emptyList())
    val receivedRequests: StateFlow<List<FriendRequest>> = _receivedRequests

    private val _sentRequestUids = MutableStateFlow<Set<String>>(emptySet())
    val sentRequestUids: StateFlow<Set<String>> = _sentRequestUids

    init {
        if (uid.isNotEmpty()) {
            loadFriends()
            loadFriendRequests()
            loadSentRequests()
        }
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

        db.collectionGroup("profile")
            .get()
            .addOnSuccessListener { profilesSnap ->
                val results = mutableListOf<SearchResult>()
                
                for (profileDoc in profilesSnap.documents) {
                    val userId = profileDoc.reference.parent.parent?.id ?: continue
                    if (userId == uid) continue
                    
                    val name = profileDoc.getString("name") ?: ""
                    val email = profileDoc.getString("email") ?: ""
                    
                    if (name.lowercase().contains(lowerQuery) || 
                        email.lowercase().contains(lowerQuery)
                    ) {
                        results.add(SearchResult(uid = userId, name = name, email = email))
                    }
                }
                
                _searchResults.value = results
                _isSearching.value = false
            }
            .addOnFailureListener {
                _searchResults.value = emptyList()
                _isSearching.value = false
            }
    }

    fun sendFriendRequest(friendUid: String) {
        if (uid.isEmpty() || friendUid == uid) return

        val timestamp = System.currentTimeMillis()

        // Add to receiver's friendRequests
        db.collection("users")
            .document(friendUid)
            .collection("friendRequests")
            .document(uid)
            .set(mapOf("uid" to uid, "timestamp" to timestamp))

        // Add to sender's sentRequests
        db.collection("users")
            .document(uid)
            .collection("sentRequests")
            .document(friendUid)
            .set(mapOf("uid" to friendUid, "timestamp" to timestamp))
    }

    fun acceptFriendRequest(friendUid: String) {
        if (uid.isEmpty()) return
        
        // Add to friends
        addFriend(friendUid)
        
        // Delete request from receiver
        db.collection("users")
            .document(uid)
            .collection("friendRequests")
            .document(friendUid)
            .delete()
            
        // Delete request from sender
        db.collection("users")
            .document(friendUid)
            .collection("sentRequests")
            .document(uid)
            .delete()
    }

    fun rejectFriendRequest(friendUid: String) {
        if (uid.isEmpty()) return

        // Delete request from receiver
        db.collection("users")
            .document(uid)
            .collection("friendRequests")
            .document(friendUid)
            .delete()
            
        // Delete request from sender
        db.collection("users")
            .document(friendUid)
            .collection("sentRequests")
            .document(uid)
            .delete()
    }

    private fun loadFriendRequests() {
        if (uid.isEmpty()) return

        db.collection("users")
            .document(uid)
            .collection("friendRequests")
            .addSnapshotListener { snap, _ ->
                if (snap == null || snap.isEmpty) {
                    _receivedRequests.value = emptyList()
                    return@addSnapshotListener
                }

                val requests = mutableListOf<FriendRequest>()
                var completed = 0

                for (doc in snap.documents) {
                    val senderUid = doc.id
                    val timestamp = doc.getLong("timestamp") ?: 0L

                    db.collection("users")
                        .document(senderUid)
                        .collection("profile")
                        .document("main")
                        .get()
                        .addOnSuccessListener { profileDoc ->
                            val name = profileDoc.getString("name") ?: "User"
                            val email = profileDoc.getString("email") ?: ""
                            requests.add(FriendRequest(senderUid, name, email, timestamp))

                            completed++
                            if (completed == snap.size()) {
                                _receivedRequests.value = requests.sortedByDescending { it.timestamp }
                            }
                        }
                        .addOnFailureListener {
                            completed++
                            if (completed == snap.size()) {
                                _receivedRequests.value = requests.sortedByDescending { it.timestamp }
                            }
                        }
                }
            }
    }

    private fun loadSentRequests() {
        if (uid.isEmpty()) return

        db.collection("users")
            .document(uid)
            .collection("sentRequests")
            .addSnapshotListener { snap, _ ->
                val sentUids = snap?.documents?.mapNotNull { it.id } ?: emptyList()
                _sentRequestUids.value = sentUids.toSet()
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
