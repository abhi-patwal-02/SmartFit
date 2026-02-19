package com.example.smartfit.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartfit.R
import com.example.smartfit.ui.theme.Black
import com.example.smartfit.ui.theme.DDBlue
import com.example.smartfit.ui.theme.GText
import com.example.smartfit.ui.theme.Grey
import com.example.smartfit.ui.theme.Orange
import com.example.smartfit.ui.theme.Red
import com.example.smartfit.ui.theme.Transparent
import com.example.smartfit.ui.theme.WText
import com.example.smartfit.viewModel.AuthViewModel
import com.example.smartfit.viewModel.OnboardingViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    onboardingViewModel: OnboardingViewModel = viewModel()
){
    val profile by onboardingViewModel.profileData.collectAsState()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(uid) {
        uid?.let { onboardingViewModel.fetchProfile(it) }
    }

    Column(modifier = Modifier.background(DDBlue)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Profile",
                color = WText,
                fontSize = 25.sp
            )
            Text(
                text = "Your Fitness Journey",
                color = GText,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            ProfileCard(
                email = profile?.email ?: "Loading...",
                name = profile?.email?.substringBefore("@") ?: "User"
            )

            Row {
                ProfileMiniCard(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(end = 4.dp),
                    painter = painterResource(R.drawable.up_trend_round_svgrepo_com),
                    text1 = "Current Weight",
                    text2 = profile?.currentWeight ?: "-"
                )

                ProfileMiniCard(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(start = 4.dp),
                    painter = painterResource(R.drawable.circle_of_fifths_svgrepo_com),
                    text1 = "Goal",
                    text2 = profile?.goal ?: "-"
                )
            }
            Row {
                ProfileMiniCard(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(end = 4.dp),
                    painter = painterResource(R.drawable.calendar_svgrepo_com),
                    text1 = "Total Workouts",
                    text2 = "47"
                )

                ProfileMiniCard(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(start = 4.dp),
                    painter = painterResource(R.drawable.up_trend_round_svgrepo_com),
                    text1 = "Calories Burned",
                    text2 = "12306"
                )
            }

            SettingsCard()

            //Sign Out Button
            Button(
                onClick = {
                    authViewModel.signOut()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Transparent),
                border = BorderStroke(2.dp, Red),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text(
                    text = "Sign Out",
                    color = Red
                )
            }
        }
    }
}

@Composable
fun SettingsCard(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Grey)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Settings",
                fontSize = 22.sp,
                color = WText,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = DDBlue),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
                ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Notification Preferences",
                        color = Color(0xFFFFFFFF)
                    )
                }
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = DDBlue),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Units and Measurements",
                        color = Color(0xFFFFFFFF))
                }
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = DDBlue),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Privacy and Security",
                        color = Color(0xFFFFFFFF))
                }
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = DDBlue),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Help and Support",
                        color = Color(0xFFFFFFFF))
                }
            }
        }
    }
}

@Composable
fun ProfileMiniCard(
    modifier: Modifier = Modifier,
    painter: Painter,
    text1: String,
    text2: String
){
    Card(
        modifier = modifier
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Grey),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painter,
                    contentDescription = "card icon",
                    modifier = Modifier.size(20.dp),
                    tint = Orange
                )
                Text(
                    text = text1,
                    color = WText,
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 12.sp
                )
            }
            Text(
                text = text2,
                color = WText,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun ProfileCard(
    name: String,
    email: String
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Orange)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = {},
                    contentPadding = PaddingValues(16.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x3A000000))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.profile_1341_svgrepo_com),
                        contentDescription = "Profile Photo",
                        modifier = Modifier.size(30.dp),
                        tint = WText
                    )
                }
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = name,
                        color = WText,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Icon(
                            painter = painterResource(R.drawable.email_svgrepo_com),
                            modifier = Modifier.size(15.dp),
                            contentDescription = "Email Logo",
                            tint = WText
                        )
                        Text(
                            text = email,
                            color = WText,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

            }
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x3A000000))
            ) {
                Text(
                    text = "Edit Profile",
                    color = Black
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview(){
    ProfileScreen()
}