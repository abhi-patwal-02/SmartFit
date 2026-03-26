package com.example.smartfit.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartfit.R
import com.example.smartfit.model.League
import com.example.smartfit.ui.theme.DDBlue
import com.example.smartfit.ui.theme.GText
import com.example.smartfit.ui.theme.Grey
import com.example.smartfit.ui.theme.LBlue
import com.example.smartfit.ui.theme.Orange
import com.example.smartfit.ui.theme.WText
import com.example.smartfit.viewModel.FriendsViewModel
import com.example.smartfit.viewModel.PointsViewModel
import com.example.smartfit.viewModel.SearchResult
import com.example.smartfit.viewModel.FriendRequest
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FriendsScreen(
    pointsViewModel: PointsViewModel = viewModel(),
) {
    // Use uid as a key so each user account gets its own ViewModel instance,
    // preventing one user's friend list from leaking into another account's session.
    val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val friendsViewModel: FriendsViewModel = viewModel(key = currentUid)
    val userPoints by pointsViewModel.userPoints.collectAsState()
    val league by pointsViewModel.league.collectAsState()
    val friends by friendsViewModel.friends.collectAsState()
    val searchResults by friendsViewModel.searchResults.collectAsState()
    val isSearching by friendsViewModel.isSearching.collectAsState()
    val friendUids by friendsViewModel.friendUids.collectAsState()
    val receivedRequests by friendsViewModel.receivedRequests.collectAsState()
    val sentRequestUids by friendsViewModel.sentRequestUids.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DDBlue)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // ── My League Card ──
            item {
                MyLeagueCard(
                    points = userPoints.totalPoints,
                    streak = userPoints.currentStreak,
                    league = league
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Search Bar ──
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        friendsViewModel.searchUsers(it)
                    },
                    placeholder = { Text("Search friends by name or email...", color = GText) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_search_24),
                            contentDescription = "Search",
                            tint = GText
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = WText,
                        unfocusedTextColor = WText,
                        cursorColor = Orange,
                        focusedBorderColor = Orange,
                        unfocusedBorderColor = Grey,
                        focusedContainerColor = Grey,
                        unfocusedContainerColor = Grey
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Search Results ──
            if (searchQuery.isNotBlank()) {
                item {
                    Text(
                        text = "Search Results",
                        color = WText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                if (isSearching) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Orange)
                        }
                    }
                } else if (searchResults.isEmpty()) {
                    item {
                        Text(
                            text = "No users found",
                            color = GText,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                } else {
                    items(searchResults) { user ->
                        SearchResultCard(
                            user = user,
                            isAlreadyFriend = friendUids.contains(user.uid),
                            isRequested = sentRequestUids.contains(user.uid),
                            onAddFriend = { friendsViewModel.sendFriendRequest(user.uid) }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            // ── Friend Requests ──
            if (receivedRequests.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Friend Requests",
                            color = WText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${receivedRequests.size} requests",
                            color = GText,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(receivedRequests) { req ->
                    FriendRequestCard(
                        request = req,
                        onAccept = { friendsViewModel.acceptFriendRequest(req.uid) },
                        onReject = { friendsViewModel.rejectFriendRequest(req.uid) }
                    )
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            // ── Friends List Header ──
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Friends Leaderboard",
                        color = WText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${friends.size} friends",
                        color = GText,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // ── Friends List ──
            if (friends.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Grey)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.friends_icon),
                                contentDescription = "No friends",
                                modifier = Modifier.size(48.dp),
                                tint = GText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No friends yet",
                                color = WText,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Search and add friends to compare your progress!",
                                color = GText,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else {
                items(friends) { friend ->
                    FriendCard(
                        rank = friends.indexOf(friend) + 1,
                        name = friend.name,
                        points = friend.totalPoints,
                        league = friend.league,
                        onRemove = { friendsViewModel.removeFriend(friend.uid) }
                    )
                }
            }
        }
    }
}

@Composable
fun MyLeagueCard(
    points: Int,
    streak: Int,
    league: League
) {
    val nextLeague = League.nextLeague(league)
    val progressToNext = if (nextLeague != null) {
        val range = nextLeague.minPoints - league.minPoints
        val progress = points - league.minPoints
        if (range > 0) progress.toFloat() / range.toFloat() else 1f
    } else 1f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            league.color.copy(alpha = 0.3f),
                            Grey
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // League badge
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(league.color.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.league_trophy),
                            contentDescription = "League",
                            modifier = Modifier.size(32.dp),
                            tint = league.color
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = league.displayName + " League",
                            color = league.color,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$points points · $streak day streak",
                            color = WText,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Progress to next league
                if (nextLeague != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = league.displayName,
                            color = league.color,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = nextLeague.displayName,
                            color = nextLeague.color,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF2B303B))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressToNext.coerceIn(0f, 1f))
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(league.color, nextLeague.color)
                                    )
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${nextLeague.minPoints - points} points to ${nextLeague.displayName}",
                        color = GText,
                        fontSize = 12.sp
                    )
                } else {
                    Text(
                        text = "🏆 You've reached the highest league!",
                        color = league.color,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(
    user: SearchResult,
    isAlreadyFriend: Boolean,
    isRequested: Boolean,
    onAddFriend: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Grey),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Orange.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.profile_1341_svgrepo_com),
                    contentDescription = "User",
                    modifier = Modifier.size(24.dp),
                    tint = Orange
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name.ifBlank { "User" },
                    color = WText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = user.email,
                    color = GText,
                    fontSize = 12.sp
                )
            }
            if (isAlreadyFriend) {
                Text(
                    text = "Added ✓",
                    color = LBlue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } else if (isRequested) {
                Text(
                    text = "Requested",
                    color = GText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Button(
                    onClick = onAddFriend,
                    colors = ButtonDefaults.buttonColors(containerColor = Orange),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = ButtonDefaults.ContentPadding
                ) {
                    Text("Add", color = WText, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun FriendRequestCard(
    request: FriendRequest,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Grey),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Orange.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.profile_1341_svgrepo_com),
                    contentDescription = "User",
                    modifier = Modifier.size(24.dp),
                    tint = Orange
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = request.name.ifBlank { "User" },
                    color = WText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = request.email,
                    color = GText,
                    fontSize = 12.sp
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onAccept,
                    colors = ButtonDefaults.buttonColors(containerColor = Orange),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = ButtonDefaults.ContentPadding
                ) {
                    Text("Accept", color = WText, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onReject,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_delete_24),
                        contentDescription = "Reject",
                        modifier = Modifier.size(20.dp),
                        tint = GText
                    )
                }
            }
        }
    }
}

@Composable
fun FriendCard(
    rank: Int,
    name: String,
    points: Int,
    league: League,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Grey),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank number
            Text(
                text = "#$rank",
                color = if (rank <= 3) Orange else GText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(36.dp)
            )

            // League badge
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(league.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.league_trophy),
                    contentDescription = "League",
                    modifier = Modifier.size(22.dp),
                    tint = league.color
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            // Name and league
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = WText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = league.displayName,
                    color = league.color,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Points
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$points",
                    color = WText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "pts",
                    color = GText,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Remove button
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_delete_24),
                    contentDescription = "Remove friend",
                    modifier = Modifier.size(18.dp),
                    tint = GText
                )
            }
        }
    }
}
