package com.example.smartfit.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.zIndex
import com.example.smartfit.R

@Composable
fun AnatomyScreen(){
    var muscleSelected by remember { mutableStateOf("Chest") }

    Column(modifier = Modifier
        .background(Color(0xFF0F131A))
        .fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x00000000)),
                    modifier = Modifier.size(30.dp),
                    contentPadding = PaddingValues(5.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back_svgrepo_com),
                        contentDescription = "Back Button",
                        tint = Color(0xFFFAFAFA)
                    )
                }
                Text(
                    text = "3D Anatomy",
                    fontSize = 25.sp,
                    color = Color(0xFFFAFAFA),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            AnatomyCard()

            SelectMuscle()
        }
    }
}

@Composable
fun AnatomyCard(){

}

@Composable
fun SelectMuscle() {
    val musclesList: List<String> = listOf(
        "Chest", "Back", "Shoulder", "Biceps", "Triceps", "Forearms",
        "Core", "Legs", "Quads", "Lower Back", "Hamstrings"
    )
    val exerciseList: List<List<String>> = listOf(
        listOf("Bench Press", "Chest Fly", "Push Up"),
        listOf("Pull Up", "Deadlift", "Bent-over Row"),
        listOf("Overhead Press", "Lateral Raise", "Front Raise"),
        listOf("Bicep Curl", "Hammer Curl"),
        listOf("Tricep Pushdown", "Dips"),
        listOf("Wrist Curl", "Reverse Wrist Curl"),
        listOf("Plank", "Russian Twist"),
        listOf("Squats", "Lunge", "Leg Press"),
        listOf("Leg Extension", "Step Up"),
        listOf("Back Extension", "Good Morning"),
        listOf("Leg Curl", "Glute Ham Raise")
    )

    var expandedMuscle by remember { mutableStateOf(false) }
    var selectedMuscle by remember { mutableStateOf(musclesList.firstOrNull() ?: "") }

    var expandedExercise by remember { mutableStateOf(false) }
    // Initially set exercises based on the first muscle group
    var exercisesForSelectedMuscle by remember { mutableStateOf(exerciseList.firstOrNull() ?: emptyList()) }
    var selectedExercise by remember { mutableStateOf(exercisesForSelectedMuscle.firstOrNull() ?: "") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C202A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Box(){
                Column() {
                    Text(
                        text = "Select Muscle Group",
                        fontSize = 25.sp,
                        color = Color(0xFFFAFAFA),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedButton(
                        onClick = { expandedMuscle = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151820)),
                        border = BorderStroke(2.dp, Color(0xFF232631))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ){
                            Text(if (selectedMuscle.isEmpty()) "Select Muscle" else selectedMuscle)
                        }

                    }
                    DropdownMenu(
                        expanded = expandedMuscle,
                        onDismissRequest = { expandedMuscle = false },
                        modifier = Modifier
                            .heightIn(min = 50.dp, max = 200.dp)
                            .zIndex(1f)
                    ) {
                        musclesList.forEachIndexed { index, muscle ->
                            DropdownMenuItem(
                                text = { Text(muscle) },
                                onClick = {
                                    selectedMuscle = muscle
                                    exercisesForSelectedMuscle = exerciseList.getOrNull(index) ?: emptyList()
                                    selectedExercise = exercisesForSelectedMuscle.firstOrNull() ?: ""
                                    expandedMuscle = false
                                }
                            )
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(20.dp))


            Box(){
                Column() {
                    Text(
                        text = "Select Exercise",
                        fontSize = 25.sp,
                        color = Color(0xFFFAFAFA),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedButton(
                        onClick = { expandedExercise = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151820)),
                        border = BorderStroke(2.dp, Color(0xFF232631))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart,
                        ){
                            Text(if (selectedExercise.isEmpty()) "Select Exercise" else selectedExercise)
                        }

                    }
                    DropdownMenu(
                        expanded = expandedExercise,
                        onDismissRequest = { expandedExercise = false },
                        modifier = Modifier
                            .heightIn(min = 50.dp, max = 200.dp)
                    ) {
                        exercisesForSelectedMuscle.forEach { exercise ->
                            DropdownMenuItem(
                                text = { Text(exercise) },
                                onClick = {
                                    selectedExercise = exercise
                                    expandedExercise = false
                                }
                            )
                        }
                    }
                }

            }

        }
    }
}


@Composable
fun SimpleDropdownMenu(options: List<String>, label: String = "Select") {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options.firstOrNull() ?: "") }

    Column {
        OutlinedButton(onClick = { expanded = true }) {
            Text(if (selectedOption.isEmpty()) label else selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun AnatomyScreenPreview(){
    AnatomyScreen()
}