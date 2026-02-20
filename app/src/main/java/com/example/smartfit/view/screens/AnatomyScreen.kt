package com.example.smartfit.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartfit.R
import com.example.smartfit.model.*
import com.example.smartfit.ui.theme.WText
import com.example.smartfit.view.components.Model3DViewer
import com.example.smartfit.view.components.destroyFilamentResources
import com.example.smartfit.view.components.setExercise

@Composable
fun AnatomyScreen(onBackNavigateToExercise: () -> Unit) {

    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }

    DisposableEffect(Unit) {
        onDispose { destroyFilamentResources() }
    }

    Column(
        modifier = Modifier
            .background(Color(0xFF0F131A))
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            destroyFilamentResources()
                            onBackNavigateToExercise()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        modifier = Modifier.size(30.dp),
                        contentPadding = PaddingValues(5.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back_svgrepo_com),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "3D Anatomy",
                        fontSize = 25.sp,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

            }
            item {
                SelectMuscle { exercise ->
                    selectedExercise = exercise
                    setExercise(exercise)   // 🔥 sends to filament
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                AnatomyCard(selectedExercise)

                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                ShowSteps()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {







        }
    }
}

@Composable
fun AnatomyCard(exercise: Exercise?) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C202A)),
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, Color(0xFF232631))
    ) {
        Model3DViewer(
            modelPath = "models/FinalYr3dModel.glb",
            exercise = exercise,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
    }
}

@Composable
fun SelectMuscle(onExerciseSelected: (Exercise) -> Unit) {

    val musclesList = listOf("Chest","Back","Shoulder","Biceps","Legs")

    val exerciseList = listOf(
        listOf("Push Up"),
        listOf("Pull Up"),
        listOf("Overhead Press"),
        listOf("Bicep Curl","Hammer Curl"),
        listOf("Squat")
    )

    var expandedMuscle by remember { mutableStateOf(false) }
    var selectedMuscle by remember { mutableStateOf("") }

    var expandedExercise by remember { mutableStateOf(false) }
    var exercisesForSelectedMuscle by remember { mutableStateOf(emptyList<String>()) }
    var selectedExerciseText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C202A)),
        border = BorderStroke(2.dp, Color(0xFF232631))
    ) {

        Row(modifier = Modifier.padding(16.dp)) {

            Column(modifier = Modifier.weight(1f)) {

                Text("Muscle Group", color = Color.White)

                OutlinedButton(
                    onClick = { expandedMuscle = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151820))
                ) {
                    Text(selectedMuscle.ifEmpty { "Select Muscle" }, color = WText)
                }

                DropdownMenu(
                    expanded = expandedMuscle,
                    onDismissRequest = { expandedMuscle = false }
                ) {
                    musclesList.forEachIndexed { index, muscle ->
                        DropdownMenuItem(
                            text = { Text(muscle) },
                            onClick = {
                                selectedMuscle = muscle
                                exercisesForSelectedMuscle = exerciseList[index]
                                expandedMuscle = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text("Exercise", color = Color.White)

                OutlinedButton(
                    onClick = { expandedExercise = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151820))
                ) {
                    Text(selectedExerciseText.ifEmpty { "Select Exercise" }, color = WText)
                }

                DropdownMenu(
                    expanded = expandedExercise,
                    onDismissRequest = { expandedExercise = false }
                ) {

                    exercisesForSelectedMuscle.forEach { exerciseName ->

                        DropdownMenuItem(
                            text = { Text(exerciseName) },
                            onClick = {
                                selectedExerciseText = exerciseName
                                expandedExercise = false

                                val exercise = when(exerciseName.lowercase()) {

                                    "bicep curl","hammer curl" ->
                                        Exercise(
                                            type = ExerciseType.BICEP_CURL,
                                            startPose = ExercisePoses.BICEP_CURL_START,
                                            endPose = ExercisePoses.BICEP_CURL_PEAK
                                        )

                                    "squat" ->
                                        Exercise(
                                            type = ExerciseType.SQUAT,
                                            startPose = ExercisePoses.SQUAT_STANDING,
                                            endPose = ExercisePoses.SQUAT_BOTTOM
                                        )

                                    else -> null
                                }

                                exercise?.let { onExerciseSelected(it) }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowSteps() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C202A)),
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, Color(0xFF232631))
    ) {
        Column(modifier = Modifier.padding(12.dp)) { }
    }
}
