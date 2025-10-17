package com.example.smartfit.view.screens

import android.R
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NutritionScreen(){
    Column(modifier = Modifier.background(Color(0xFF0F131A))){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ){
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
                        CircularStat(value = 381f, maxValue = 2000f, label = "Calories")
                        CircularStat(value = 36f, maxValue = 150f, label = "Protein", unit = "g")
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CircularStat(value = 45f, maxValue = 250f, label = "Carbs", unit = "g")
                        CircularStat(value = 5.4f, maxValue = 65f, label = "Fat", unit = "g")
                    }
                }
            }
            SearchBar()
        }
    }
}

@Composable
fun SearchBar(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(Color(0xFF1C202A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF151820)),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {

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