package com.example.smartfit.view.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.smartfit.R
import com.example.smartfit.model.ExerciseItem
import com.example.smartfit.view.components.CustomTextField
//Color Imports
import com.example.smartfit.ui.theme.DDBlue
import com.example.smartfit.ui.theme.GText
import com.example.smartfit.ui.theme.Grey
import com.example.smartfit.ui.theme.LBlue
import com.example.smartfit.ui.theme.Orange
import com.example.smartfit.ui.theme.WText

import com.example.smartfit.ui.theme.Transparent
import com.example.smartfit.viewModel.ExerciseSearchViewModel
import com.example.smartfit.viewModel.WorkoutViewModel

@Composable
fun ExerciseScreen(
    onNavigateToAnatomy: () -> Unit,
    workoutVM: WorkoutViewModel = viewModel(),
    searchVM: ExerciseSearchViewModel = viewModel()
) {

    val workouts by workoutVM.workouts.collectAsState()

    Column(
        modifier = Modifier
            .background(DDBlue)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            item {
                Text("Workout Tracker", fontSize = 25.sp, color = WText)
                Text("Log your exercises and track progress", color = GText)
            }
            item {
                TodayProgressBar(1, 3)
            }
            item {
                GoTo3DModel(onNavigateToAnatomy)
            }
            item {
                AddExercise(
                    workoutVM = workoutVM,
                    searchVM = searchVM
                )
            }
            item {
                ExerciseLogs(workouts)
            }
        }
    }
}
@Composable
fun ExerciseLogs(workouts: List<com.example.smartfit.model.WorkoutLog>) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Grey)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("Today's Workouts", fontSize = 25.sp, color = WText)

            workouts.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(it.name, color = WText)
                        Text(
                            "${it.sets}x${it.reps}  •  ${it.weight}kg",
                            color = GText,
                            fontSize = 12.sp
                        )
                    }
                    Text("${it.calories.toInt()} kcal", color = Orange)
                }
            }
        }
    }
}


@Composable
fun AddExercise(
    workoutVM: WorkoutViewModel,
    searchVM: ExerciseSearchViewModel
) {

    val query by searchVM.query.collectAsState()
    val results by searchVM.results.collectAsState()

    var selectedExercise by remember { mutableStateOf<ExerciseItem?>(null) }
    var setNum by remember { mutableStateOf("") }
    var repNum by remember { mutableStateOf("") }
    var wtQuantity by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Grey)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text("Add Exercise", fontSize = 25.sp, color = WText)

            // 🔎 SEARCH FIELD (same UI)
            CustomTextField(
                value = query,
                onValueChange = { searchVM.onQueryChange(it) },
                placeholder = "Exercise Name"
            )

            // 🔎 SUGGESTIONS
            results.take(5).forEach { exercise ->
                Text(
                    text = exercise.name,
                    color = WText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedExercise = exercise
                            searchVM.onQueryChange(exercise.name)
                        }
                        .padding(8.dp)
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {

                Column(
                    modifier = Modifier.fillMaxWidth(0.33f)
                        .padding(vertical = 8.dp)
                        .padding(end = 4.dp)
                ) {
                    Text("Sets", fontSize = 10.sp, color = GText)
                    CustomTextField(setNum, { setNum = it }, )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(0.5f)
                        .padding(vertical = 8.dp)
                        .padding(end = 4.dp)
                ) {
                    Text("Reps", fontSize = 10.sp, color = GText)
                    CustomTextField(repNum, { repNum = it }, )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(1f)
                        .padding(vertical = 8.dp)
                ) {
                    Text("Weight", fontSize = 10.sp, color = GText)
                    CustomTextField(wtQuantity, { wtQuantity = it }, )
                }
            }

            Button(
                onClick = {
                    val ex = selectedExercise ?: return@Button

                    workoutVM.addWorkout(
                        ex = ex,
                        sets = setNum.toIntOrNull() ?: 0,
                        reps = repNum.toIntOrNull() ?: 0,
                        weight = wtQuantity.toFloatOrNull() ?: 0f
                    )

                    setNum = ""
                    repNum = ""
                    wtQuantity = ""
                    searchVM.onQueryChange("")
                    selectedExercise = null
                },
                colors = ButtonDefaults.buttonColors(containerColor = Orange)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_add_24),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = WText
                    )
                    Text("Add Exercise", color = WText)
                }
            }
        }
    }
}



@Composable
fun GoTo3DModel(onNavigateToAnatomy: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Grey)) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column {
                Text(
                    text = "View 3D Anatomy Model",
                    fontSize = 22.sp,
                    color = WText
                )
                Text(
                    text = "Learn Correct Form for any Exercise.",
                    color = GText
                )
            }
            Button(
                onClick = onNavigateToAnatomy,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Transparent)
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_play_arrow_24),
                    contentDescription = "Go to 3D model.",
                    modifier = Modifier.size(40.dp),
                    tint = Orange
                )
            }
        }
    }
}
@Composable
fun TodayProgressBar(value: Int, maxValue: Int){
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LBlue)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Today's Progress")
                Text("$value/$maxValue", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CircularPercent(value = value, maxValue = maxValue, label = "%")
        }
    }
}

@Composable
fun CircularPercent(
    modifier: Modifier = Modifier,
    value: Int,
    maxValue: Int,
    unit: String = "",
    label: String,
    arcColor: Color = WText,
    trackColor: Color = Color(0x22FFFFFF)
) {


    Box(
        modifier = Modifier.size(50.dp),
        contentAlignment = Alignment.Center
    ) {
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
                sweepAngle = 360f * (value.toFloat() / maxValue.toFloat()),
                useCenter = false,
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Text("${(value * 100) / maxValue}%")
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExerciseScreenPreview(){
    val navController = rememberNavController()
    ExerciseScreen(onNavigateToAnatomy = {
        navController.navigate("anatomy")
    })
}