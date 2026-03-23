package com.example.smartfit.view.screens

import com.example.smartfit.R
import android.graphics.Paint
import android.health.connect.datatypes.NutritionRecord
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartfit.model.FoodItem
import com.example.smartfit.viewModel.FoodSearchViewModel
import com.example.smartfit.viewModel.NutritionViewModel
import com.example.smartfit.viewModel.DashboardViewModel
import androidx.compose.runtime.LaunchedEffect

@Composable
fun NutritionScreen(
    searchVM: FoodSearchViewModel = viewModel(),
    nutritionVM: NutritionViewModel = viewModel(),
    dashboardVM: DashboardViewModel = viewModel()
){
    val foods by nutritionVM.foods.collectAsState()

    // Compute live totals
    val totalCalories = foods.sumOf { it.calories }
    val totalProtein = foods.sumOf { it.protein }
    val totalCarbs = foods.sumOf { it.carbs }
    val totalFat = foods.sumOf { it.fat }

    // Load targets
    LaunchedEffect(Unit) {
        dashboardVM.loadTargets()
    }

    val calorieTarget = if (dashboardVM.caloriesTarget > 0) dashboardVM.caloriesTarget else 2000
    val proteinTarget = if (dashboardVM.proteinTarget > 0) dashboardVM.proteinTarget else 150
    // Rough estimates for carbs/fat targets based on calories
    val carbsTarget = (calorieTarget * 0.45 / 4).toInt()   // 45% of cals from carbs
    val fatTarget = (calorieTarget * 0.25 / 9).toInt()      // 25% of cals from fat

    Column(modifier = Modifier.background(Color(0xFF0F131A))){
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            item {
                Text(
                    text = "Nutrition Tracker",
                    color = Color(0xFFFAFAFA),
                    fontSize = 25.sp
                )
                Text(
                    text = "Track your daily food intake",
                    color = Color(0xFFA6A6A6),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFF7043)),
                ){
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Today's Total",
                            color = Color(0xFFFAFAFA),
                            fontSize = 22.sp
                        )
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            CircularStat(value = totalCalories.toFloat(), maxValue = calorieTarget.toFloat(), label = "Calories")
                            CircularStat(value = totalProtein.toFloat(), maxValue = proteinTarget.toFloat(), label = "Protein", unit = "g")
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            CircularStat(value = totalCarbs.toFloat(), maxValue = carbsTarget.toFloat(), label = "Carbs", unit = "g")
                            CircularStat(value = totalFat.toFloat(), maxValue = fatTarget.toFloat(), label = "Fat", unit = "g")
                        }
                    }
                }
            }
            item {
                SearchBar(
                    searchVM = searchVM,
                    onFoodSelected = { food ->
                        nutritionVM.addFood(food)
                    }
                )


            }
            item {

                NutritionRecord(
                    foods = foods,
                    onDelete = { nutritionVM.deleteFood(it.docId) }
                )
            }
        }

    }
}

@Composable
fun NutritionRecord(
    foods: List<FoodItem>,
    onDelete: (FoodItem) -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(Color(0xFF1C202A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("Today's Meals", fontSize = 22.sp, color = Color.White)

            foods.forEach {
                NutritionRecordItem(it, onDelete)
            }
        }
    }
}


@Composable
fun NutritionRecordItem(
    food: FoodItem,
    onDelete: (FoodItem) -> Unit
){
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF151820)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)) {
            Text(
                text = food.name,
                color = Color(0xFFFAFAFA)
            )
            Text(
                text = "${food.calories} cal • P: ${food.protein}g • C: ${food.carbs}g • F: ${food.fat}g",
                color = Color(0xFFA6A6A6)
            )
        }
        Image(painter = painterResource(R.drawable.outline_delete_24), contentDescription = "Delete",
            modifier = Modifier.padding(end = 12.dp).clickable { onDelete(food) }
        )

    }
}

@Composable
fun SearchBar(
    searchVM: FoodSearchViewModel,
    onFoodSelected: (FoodItem) -> Unit
) {

    val query by searchVM.query.collectAsState()
    val results by searchVM.results.collectAsState()

    Column {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(Color(0xFF1C202A))
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color(0xFF232631), RoundedCornerShape(8.dp))
                    .background(Color(0xFF151820))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {

                BasicTextField(
                    value = query,
                    onValueChange = { searchVM.onQueryChange(it) },
                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                    decorationBox = { inner ->
                        if (query.isEmpty()) {
                            Text("Add food", color = Color(0xFFA6A6A6))
                        }
                        inner()
                    }
                )
            }
        }

        // suggestions list
        results.take(5).forEach { food ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                colors = CardDefaults.cardColors(Color(0xFF151820))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onFoodSelected(food)
                            searchVM.onQueryChange("")
                        }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(food.name, color = Color.White)
                    Text("${food.calories} kcal", color = Color.Gray)
                }
            }
        }
    }
}


@Composable
fun CircularStat(
    modifier: Modifier = Modifier,
    value: Float,
    maxValue: Float,
    unit: String = "",
    label: String,
    arcColor: Color = Color.White,
    trackColor: Color = Color(0x22FFFFFF)
){
    Column(
        modifier = modifier.size(150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(bottom = 4.dp),
            color = Color.White
        )

        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ){
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Draw grey track
                drawArc(
                    color = trackColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )
                // Draw progress arc
                drawArc(
                    color = arcColor,
                    startAngle = -90f,
                    sweepAngle = 360f * (value / maxValue),
                    useCenter = false,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Text(
                text = value.toInt().toString() + unit,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.White
            )
        }
        if (unit.isNotEmpty()) {
            Text(
                text = "of ${maxValue.toInt()}$unit",
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp)
            )
        }else{
            Text(
                text = "of ${maxValue.toInt()}",
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NutritionScreenPreview(){
    NutritionScreen()
}