package com.example.smartfit.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartfit.R
import com.example.smartfit.ui.theme.SmartFitTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(){


    Column(modifier = Modifier.background(Color(0xFF0F131A))) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.hero_fitness),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                contentDescription = "Home page bg1",
                contentScale = ContentScale.FillWidth
            )

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

            RecentActivity()

            Row {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF7043),   // Background color
                        contentColor = Color(0xFFFAFAFA)             // Text color
                    ),
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                ) {
                    Text("Log Food")
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BFFF),   // Background color
                        contentColor = Color.Black             // Text color
                    ),
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                ) {
                    Text("Start Workout")
                }
            }

        }
    }

}

@Composable
fun RecentActivity(){
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171B23))
    ) {
        Text("Recent Activity", color = Color(0xFFFAFAFA))
    }
}

@Composable
fun FCard(
    painter: Painter,
    contentDescription: String,
    maxValue: Float,
    value: Float,
    unit: String,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171B23)),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.padding(bottom = 4.dp)) {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .height(20.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = contentDescription,
                    color = Color(0xFFFAFAFA),
                    fontSize = 12.sp
                )
            }
            Row {
                Text(
                    text = value.toInt().toString(),
                    fontSize = 20.sp,
                    color = Color(0xFFFAFAFA)
                )
                Text(
                    text = '/' + maxValue.toInt().toString() + unit,
                    fontSize = 12.sp,
                    color = Color(0xFFA6A6A6)
                )
            }
            TwoColorProgressBar(value=value, maxValue = maxValue)

        }

    }
}

@Composable
fun TwoColorProgressBar(
    value: Float,
    maxValue: Float,
    modifier: Modifier = Modifier,
    barHeight: Float = 6f,
    trackColor: Color = Color(0xFF00BFFF),
    progressColor: Color = Color(0xFFFF7043)
) {
    val progress = (value / maxValue).coerceIn(minimumValue = 0f, maximumValue = 1f)
    Box(modifier = modifier.fillMaxWidth()) {
        // Background full width bar (track)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight.dp)
                .background(trackColor)
        )
        // Progress completed portion
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(barHeight.dp)
                .background(progressColor)
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview(){
    SmartFitTheme {
        DashboardScreen()
    }
}

@Composable
fun BottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF171B23) // Your theme
    ) {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                icon = { Icon(painterResource(item.icon), contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}