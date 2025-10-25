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
import com.example.smartfit.R

@Composable
fun ProfileScreen(){
    Column(modifier = Modifier.background(Color(0xFF0F131A))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Profile",
                color = Color(0xFFFAFAFA),
                fontSize = 25.sp
            )
            Text(
                text = "Your Fitness Journey",
                color = Color(0xFFA6A6A6),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            ProfileCard()

            Row {
                ProfileMiniCard(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(end = 4.dp),
                    painter = painterResource(R.drawable.up_trend_round_svgrepo_com),
                    text1 = "Current Weight",
                    text2 = "75"
                )

                ProfileMiniCard(
                    modifier = Modifier
                        .weight(weight = 1f).padding(start = 4.dp),
                    painter = painterResource(R.drawable.circle_of_fifths_svgrepo_com),
                    text1 = "Goal",
                    text2 = "78"
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
                        .weight(weight = 1f).padding(start = 4.dp),
                    painter = painterResource(R.drawable.up_trend_round_svgrepo_com),
                    text1 = "Calories Burned",
                    text2 = "12306"
                )
            }

            SettingsCard()

            //Sign Out Button
            Button(
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x00000000)),
                border = BorderStroke(2.dp, Color(0xFFEF433C)),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text(
                    text = "Sign Out",
                    color = Color(0xFFEF433C)
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171B23))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Settings",
                fontSize = 22.sp,
                color = Color(0xFFFAFAFA),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F131A)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
                ) {
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                    Text("Notification Preferences")
                }
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F131A)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                    Text("Units and Measurements")
                }
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F131A)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                    Text("Privacy and Security")
                }
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F131A)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                    Text("Help and Support")
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171B23)),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painter,
                    contentDescription = "card icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFFFF7043)
                )
                Text(
                    text = text1,
                    color = Color(0xFFFAFAFA),
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 12.sp
                )
            }
            Text(
                text = text2,
                color = Color(0xFFFAFAFA),
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun ProfileCard(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF7043))
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
                        tint = Color(0xFFFAFAFA)
                    )
                }
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = "John Doe",
                        color = Color(0xFFFAFAFA),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Icon(
                            painter = painterResource(R.drawable.email_svgrepo_com),
                            modifier = Modifier.size(15.dp),
                            contentDescription = "Email Logo",
                            tint = Color(0xFFFAFAFA)
                        )
                        Text(
                            text = "john.doe@example.com",
                            color = Color(0xFFFAFAFA),
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
                    color = Color(0xFF000000)
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