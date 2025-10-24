package com.example.smartfit.view.screens

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
                FCard(
                    painter = painterResource(R.drawable.flame_svgrepo_com),
                    contentDescription = "Calories",
                    maxValue = 2200f,
                    value = 1847f,
                    unit = " Kcal",
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(end = 4.dp)
                )
                FCard(
                    painter = painterResource(R.drawable.circle_of_fifths_svgrepo_com),
                    contentDescription = "Protein",
                    maxValue = 150f,
                    value = 128f,
                    unit = " g",
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(start = 4.dp)
                )
            }

            Row {
                FCard(
                    painter = painterResource(R.drawable.heartbeat_svgrepo_com),
                    contentDescription = "Workouts",
                    maxValue = 5f,
                    value = 4f,
                    unit = "",
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(end = 4.dp)
                )
                FCard(
                    painter = painterResource(R.drawable.up_trend_round_svgrepo_com),
                    contentDescription = "Weight",
                    maxValue = 80f,
                    value = 75f,
                    unit = " kg",
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(start = 4.dp)
                )
            }
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