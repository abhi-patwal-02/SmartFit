package com.example.smartfit.view.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartfit.R
import com.example.smartfit.ui.theme.BgDeep
import com.example.smartfit.ui.theme.CardBg
import com.example.smartfit.ui.theme.Cyan
import com.example.smartfit.ui.theme.Green
import com.example.smartfit.ui.theme.Orange
import com.example.smartfit.ui.theme.SmartFitTheme
import com.example.smartfit.ui.theme.TextGrey
import com.example.smartfit.ui.theme.TextWhite
import com.example.smartfit.viewModel.ActivityItem
import com.example.smartfit.viewModel.ActivityType
import com.example.smartfit.viewModel.DashboardViewModel
import com.example.smartfit.viewModel.NutritionViewModel
import com.example.smartfit.viewModel.WeekDaySummary



import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay

// ─────────────────────────────────────────────────────────────────────────────
// DashboardScreen
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DashboardScreen() {

    val dashboardVm: DashboardViewModel = viewModel()
    val nutritionVm: NutritionViewModel = viewModel()

    val foods        by nutritionVm.foods.collectAsState()
    val recentItems  by dashboardVm.recentActivity.collectAsState()
    val weeklyData   by dashboardVm.weeklyData.collectAsState()

    LaunchedEffect(foods) { dashboardVm.updateTotals(foods) }

    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { refreshing = true }
    )

    LaunchedEffect(refreshing) {
        if (refreshing) {
            dashboardVm.refreshData()
            delay(800)
            refreshing = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .background(BgDeep)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // Hero image
            item {
                Image(
                    painter = painterResource(id = R.drawable.hero_fitness),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    contentDescription = "Home page bg",
                    contentScale = ContentScale.FillWidth
                )
            }

            // Feature 3 ── Burned vs Consumed balance card
            item {
                CalorieBalanceCard(
                    consumed = dashboardVm.caloriesConsumed.toFloat(),
                    burned   = dashboardVm.caloriesBurnedToday,
                    target   = dashboardVm.caloriesTarget.toFloat()
                )
            }

            // Existing nutrition/protein cards
            item {
                Row {
                    FCard(
                        painter            = painterResource(R.drawable.flame_svgrepo_com),
                        contentDescription = "Calories",
                        maxValue           = dashboardVm.caloriesTarget.toFloat(),
                        value              = dashboardVm.caloriesConsumed.toFloat(),
                        unit               = " Kcal",
                        modifier           = Modifier.weight(1f).padding(end = 4.dp)
                    )
                    FCard(
                        painter            = painterResource(R.drawable.circle_of_fifths_svgrepo_com),
                        contentDescription = "Protein",
                        maxValue           = dashboardVm.proteinTarget.toFloat(),
                        value              = dashboardVm.proteinConsumed.toFloat(),
                        unit               = " g",
                        modifier           = Modifier.weight(1f).padding(start = 4.dp)
                    )
                }
            }

            // Feature 2 ── Weekly calorie charts (Separated)
            item {
                Column {
                    WeeklyConsumedChart(data = weeklyData)
                    Spacer(Modifier.height(8.dp))
                    WeeklyBurnedChart(data = weeklyData)
                }
            }

            // Feature 1 ── Recent Activity feed
            item {
                RecentActivity(items = recentItems)
            }

            // Action buttons
//            item {
//                Row {
//                    Button(
//                        onClick = {},
//                        colors  = ButtonDefaults.buttonColors(
//                            containerColor = Orange,
//                            contentColor   = TextWhite
//                        ),
//                        modifier = Modifier.weight(1f).padding(end = 4.dp)
//                    ) { Text("Log Food") }
//
//                    Button(
//                        onClick = {},
//                        colors  = ButtonDefaults.buttonColors(
//                            containerColor = Cyan,
//                            contentColor   = Color.Black
//                        ),
//                        modifier = Modifier.weight(1f).padding(start = 4.dp)
//                    ) { Text("Start Workout") }
//                }
//            }
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Orange,
            backgroundColor = CardBg
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Feature 3 ── Calorie Balance Card
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun CalorieBalanceCard(consumed: Float, burned: Float, target: Float) {
    val net   = consumed - burned
    val color = if (net <= target) Green else Orange

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Today's Calorie Balance",
                color      = TextWhite,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 14.sp
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BalanceStat(label = "Eaten",    value = consumed.toInt(), color = Orange)
                BalanceStat(label = "Burned",   value = burned.toInt(),   color = Green)
                BalanceStat(label = "Net",       value = net.toInt(),     color = color)
                BalanceStat(label = "Goal",      value = target.toInt(),  color = Cyan)
            }
            Spacer(Modifier.height(10.dp))
            // Net vs goal progress bar
            val progress = if (target > 0f) (net / target).coerceIn(0f, 1f) else 0f
            TwoColorProgressBar(value = net.coerceAtLeast(0f), maxValue = target.coerceAtLeast(1f), trackColor = Green, progressColor = color)
        }
    }
}

@Composable
private fun BalanceStat(label: String, value: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value.toString(), color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = label, color = TextGrey, fontSize = 11.sp)
    }
}

//@Composable
//private fun BalanceStat(label: String, value: Int, color: Color) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(text = value.toString(), color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
//        Text(text = label, color = TextGrey, fontSize = 11.sp)
//    }
//}

// ─────────────────────────────────────────────────────────────────────────────
// Feature 2 ── Weekly Calorie Chart
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun WeeklyConsumedChart(data: List<WeekDaySummary>) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Weekly Consumed",
                color      = TextWhite,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 14.sp
            )
            Spacer(Modifier.height(8.dp))

            if (data.isEmpty()) {
                Text("No data yet", color = TextGrey, fontSize = 12.sp)
                return@Column
            }

            val maxVal = data.maxOf { it.caloriesConsumed }.coerceAtLeast(1f)
            val chartHeight = 80.dp
            val barWidth    = 24.dp

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Bottom
            ) {
                var selectedIndex by remember { mutableStateOf(-1) }

                data.forEachIndexed { index, day ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier            = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                indication = null
                            ) {
                                selectedIndex = if (selectedIndex == index) -1 else index
                            }
                    ) {
                        Row(
                            verticalAlignment     = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center,
                            modifier              = Modifier.height(chartHeight)
                        ) {
                            val consumedFrac by animateFloatAsState(
                                targetValue = (day.caloriesConsumed / maxVal).coerceIn(0f, 1f) + 0.05f,
                                animationSpec = tween(600),
                                label = "consumed"
                            )
                            Box(
                                modifier = Modifier
                                    .width(barWidth)
                                    .fillMaxHeight(consumedFrac)
                                    .background(Orange, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        if (selectedIndex == index) {
                            Text("${day.caloriesConsumed.toInt()}", color = Orange, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Text(day.dayLabel, color = TextGrey, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyBurnedChart(data: List<WeekDaySummary>) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Weekly Burned",
                color      = TextWhite,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 14.sp
            )
            Spacer(Modifier.height(8.dp))

            if (data.isEmpty()) {
                Text("No data yet", color = TextGrey, fontSize = 12.sp)
                return@Column
            }

            val maxVal = data.maxOf { it.caloriesBurned }.coerceAtLeast(1f)
            val chartHeight = 80.dp
            val barWidth    = 24.dp

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Bottom
            ) {
                var selectedIndex by remember { mutableStateOf(-1) }

                data.forEachIndexed { index, day ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier            = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                indication = null
                            ) {
                                selectedIndex = if (selectedIndex == index) -1 else index
                            }
                    ) {
                        Row(
                            verticalAlignment     = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center,
                            modifier              = Modifier.height(chartHeight)
                        ) {
                            val burnedFrac by animateFloatAsState(
                                targetValue = (day.caloriesBurned / maxVal).coerceIn(0f, 1f) + 0.05f,
                                animationSpec = tween(600),
                                label = "burned"
                            )
                            Box(
                                modifier = Modifier
                                    .width(barWidth)
                                    .fillMaxHeight(burnedFrac)
                                    .background(Green, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        if (selectedIndex == index) {
                            Text("${day.caloriesBurned.toInt()}", color = Green, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Text(day.dayLabel, color = TextGrey, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Feature 1 ── Recent Activity Feed
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun RecentActivity(items: List<ActivityItem>) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Recent Activity",
                color      = TextWhite,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 14.sp
            )
            Spacer(Modifier.height(8.dp))

            if (items.isEmpty()) {
                Text(
                    "No activity logged today. Start working out or log a meal!",
                    color    = TextGrey,
                    fontSize = 12.sp
                )
            } else {
                items.forEach { item ->
                    ActivityRow(item = item)
                    if (item != items.last()) {
                        Divider(color = Color(0xFF252B36), thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityRow(item: ActivityItem) {
    val isWorkout  = item.type == ActivityType.WORKOUT
    val dotColor   = if (isWorkout) Cyan else Orange
    val iconRes    = if (isWorkout) R.drawable.heartbeat_svgrepo_com else R.drawable.flame_svgrepo_com

    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Colored dot indicator
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(dotColor, CircleShape)
        )
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.label,  color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(item.detail, color = TextGrey,  fontSize = 11.sp)
        }
        Image(
            painter            = painterResource(iconRes),
            contentDescription = null,
            modifier           = Modifier.size(18.dp)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Existing helpers (unchanged)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun FCard(
    painter: Painter,
    contentDescription: String,
    maxValue: Float,
    value: Float,
    unit: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier.padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBg),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.padding(bottom = 4.dp)) {
                Image(
                    painter            = painter,
                    contentDescription = contentDescription,
                    modifier           = Modifier.height(20.dp).align(Alignment.CenterVertically)
                )
                Text(text = contentDescription, color = TextWhite, fontSize = 12.sp)
            }
            Row {
                Text(text = value.toInt().toString(), fontSize = 20.sp, color = TextWhite)
                Text(
                    text     = '/' + maxValue.toInt().toString() + unit,
                    fontSize = 12.sp,
                    color    = TextGrey
                )
            }
            TwoColorProgressBar(value = value, maxValue = maxValue)
        }
    }
}

@Composable
fun TwoColorProgressBar(
    value: Float,
    maxValue: Float,
    modifier: Modifier = Modifier,
    barHeight: Float = 6f,
    trackColor: Color = Cyan,
    progressColor: Color = Orange
) {
    val progress = if (maxValue <= 0f) 0f else (value / maxValue).coerceIn(0f, 1f)
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight.dp)
                .background(trackColor)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(barHeight.dp)
                .background(progressColor)
        )
    }
}

@Composable
fun BottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(containerColor = CardBg) {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick  = { onItemSelected(index) },
                icon     = { Icon(painterResource(item.icon), contentDescription = item.label) },
                label    = { Text(item.label) }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    SmartFitTheme { DashboardScreen() }
}