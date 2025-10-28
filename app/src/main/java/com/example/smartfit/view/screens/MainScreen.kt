package com.example.smartfit.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartfit.R
import com.example.smartfit.ui.theme.SmartFitTheme

data class NavItem(val label: String, val icon: Int) // icon: drawable resource id

val navItems = listOf(
    NavItem("Dashboard", R.drawable.home_svgrepo_com),
    NavItem("Nutrition", R.drawable.apple_svgrepo_com),
    NavItem("Exercise", R.drawable.dumbell_svgrepo_com),
    NavItem("AI Coach", R.drawable.chat_dots_svgrepo_com),
    NavItem("Profile", R.drawable.profile_1341_svgrepo_com)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedIndex by remember { mutableStateOf(4) }

    Column(modifier = Modifier.background(Color(0xFF181D25))) {

        TopAppBar(
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center                ) {
                    Text("SmartFit", color = Color(0xFFFF7043))
                    Spacer(Modifier.weight(0.6f))
                    Image(
                        painter = painterResource(R.drawable.night_clear_svgrepo_com),
                        contentDescription = "Theme",
                        modifier = Modifier.height(20.dp)
                    )
                }
            },
            colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF13171f)
            ),
            modifier = Modifier.height(80.dp)
        )


        // Screen content area
        Box(modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
        ) {
            when (selectedIndex) {
                0 -> DashboardScreen()     // ✅ Calls another composable
                1 -> NutritionScreen()
                2 -> ExerciseScreen()
                3 -> AICoachScreen()
                4 -> ProfileScreen()
            }
        }

        // Bottom Navigation
        CustomBottomNavBar(selectedIndex) { newIndex -> selectedIndex = newIndex }
    }
}

@Composable
fun CustomBottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .background(Color(0xFF171B23))
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItems.forEachIndexed { index, item ->
            val isSelected = index == selectedIndex
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onItemSelected(index) }
            ) {
                Icon(
                    painter = painterResource(item.icon),
                    contentDescription = item.label,
                    tint = if (isSelected) Color(0xFFFF7043) else Color(0xFFA6A6A6),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = item.label,
                    fontSize = 12.sp,
                    color = if (isSelected) Color(0xFFFF7043) else Color(0xFFA6A6A6)
                )
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(4.dp)
                            .background(Color(0xFFFF7043), shape = CircleShape)
                    )
                } else {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainScreenPreview(){
    SmartFitTheme {
        MainScreen()
    }
}