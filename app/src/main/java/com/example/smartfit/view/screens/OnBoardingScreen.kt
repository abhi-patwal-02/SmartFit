package com.example.smartfit.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartfit.R
import com.example.smartfit.view.components.CustomTextField
import com.example.smartfit.view.components.DatePickerField
import com.example.smartfit.viewModel.OnboardingViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.smartfit.util.HealthCalculator
import com.example.smartfit.view.components.DobPickerField
import java.lang.String.format
import java.util.Calendar
import java.util.Locale

@Composable
fun OnBoardingScreen(
    onboardingViewModel: OnboardingViewModel = viewModel(),
    onComplete: () -> Unit

) {

    var currWt by remember { mutableStateOf("") }
    var targetWt by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var dob by remember { mutableStateOf("") }
    var selectedGoal by remember { mutableStateOf("Lose Weight") }

    // Define save function here
    val saveOnboardingDetails = {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {

            // --- convert safely ---
            val weight = currWt.toFloatOrNull() ?: 0f
            val h = height.toFloatOrNull() ?: 0f

            // --- calculate age ---
            val age = HealthCalculator.calculateAge(dob)

            // --- calculate intake ---
            val calories = HealthCalculator.calculateCalories(
                weight = weight,
                height = h,
                age = age,
                gender = gender,
                goal = selectedGoal
            )

            val protein = HealthCalculator.calculateProtein(
                weight = weight,
                goal = selectedGoal
            )

            println("AGE = $age")
            println("CALORIES = $calories")
            println("PROTEIN = $protein")

            onboardingViewModel.saveOnboardingDetails(
                uid = uid,
                currWt = currWt,
                targetWt = targetWt,
                height = height,
                goal = selectedGoal,
                gender = gender,
                dob = dob,
                calories = calories,
                protein = protein,
                onComplete = onComplete
            )
        }
    }


    Column(
        modifier = Modifier.background(Color(0xFF0F131A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnBoardingCard(
                currWt = currWt,
                onCurrWtChange = {currWt = it},
                targetWt = targetWt,
                onTargetWtChange = {targetWt = it},
                height = height,
                onHeightChange = {height = it},
                selectedGoal = selectedGoal,
                onSelectedGoalChange = {selectedGoal = it},
                gender = gender,
                onGenderChange = {gender = it},
                dob = dob,
                onDobChange = {dob = it},
                saveOnboardingDetails = saveOnboardingDetails
            )
        }
    }
}

@Composable
fun OnBoardingCard(
    currWt: String,
    onCurrWtChange: (String)-> Unit,
    targetWt: String,
    onTargetWtChange: (String)->Unit,
    height: String,
    onHeightChange: (String)->Unit,
    selectedGoal: String,
    onSelectedGoalChange: (String)->Unit,
    gender: String,
    onGenderChange: (String) -> Unit,
    dob: String,
    onDobChange: (String) -> Unit,
    saveOnboardingDetails: ()->Unit
){

    var showDatePicker by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171B23))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = Color(0xFFFF7043),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.dumbell_svgrepo_com),
                    contentDescription = "logo",
                    tint = Color(0xFFFAFAFA),
                    modifier = Modifier.size(30.dp)
                )
            }

            Text(
                text = "Complete Your Profile",
                fontSize = 22.sp,
                color = Color(0xFFFAFAFA),
                modifier = Modifier.padding(top = 24.dp)
            )

            Column {
                Text(
                    text = "Current Weight (kg)",
                    color = Color(0xFFFAFAFA),
                    modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
                )
                CustomTextField(
                    value = currWt,
                    onValueChange = onCurrWtChange,
                    placeholder = "70",
                )
                Text(
                    text = "target Weight (kg)",
                    color = Color(0xFFFAFAFA),
                    modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
                )
                CustomTextField(
                    value = targetWt,
                    onValueChange = onTargetWtChange,
                    placeholder = "78"
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Date of Birth",
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp, top = 16.dp, end = 8.dp)
                    )
                    IconButton(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.calendar_svgrepo_com),
                            contentDescription = null,
                            tint = Color(0xFFFFFFFF)
                        )
                    }
                }

                CustomTextField(
                    value = dob,
                    onValueChange = {},
                    placeholder = "dd-mm-yyyy",
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Height (cm)",
                    color = Color(0xFFFAFAFA),
                    modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
                )
                CustomTextField(
                    value = height,
                    onValueChange = onHeightChange,
                    placeholder = "185"
                )
                Text(
                    text = "Gender",
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp, top = 16.dp)
                )

                GenderGroup(
                    selectedGender = gender,
                    onGenderSelected = onGenderChange
                )

            }

            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Fitness Goals",
                    color = Color(0xFFFAFAFA),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                RadioGroup(
                    selectedGoal = selectedGoal,
                    onGoalSelected = onSelectedGoalChange
                )

            }

            Button(
                onClick = {
                    saveOnboardingDetails()

                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Complete Setup",
                    color = Color(0xFFFAFAFA),
                    fontSize = 16.sp
                )
            }

        }
    }

    if (showDatePicker) {

        val context = LocalContext.current
        val calendar = Calendar.getInstance()

        android.app.DatePickerDialog(
            context,
            { _, y, m, d ->

                val formatted = format(
                    Locale.getDefault(),
                    "%04d-%02d-%02d",
                    y,
                    m + 1,
                    d
                )

                onDobChange(formatted)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
            show()
        }
    }
}

@Composable
fun GenderGroup(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    val genders = listOf("Male", "Female")

    Column {
        genders.forEach { g ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = g == selectedGender,
                    onClick = { onGenderSelected(g) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFFFF7043),
                        unselectedColor = Color(0xFFFF7043)
                    ),
                    modifier = Modifier.height(30.dp)
                )
                Text(g, color = Color.White)
            }
        }
    }
}

@Composable
fun RadioGroup(
    selectedGoal: String,
    onGoalSelected: (String) -> Unit
) {
    val goals = listOf("Lose Weight", "Gain Muscle", "Maintain Weight")
    Column {
        goals.forEach { goal ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = goal == selectedGoal,
                    onClick = { onGoalSelected(goal) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFFFF7043), // orange
                        unselectedColor = Color(0xFFFF7043), // orange border
                        disabledSelectedColor = Color.LightGray
                    ),
                    modifier = Modifier.height(30.dp)
                )
                Text(
                    text = goal,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnBoardingScreenPreview(){
    OnBoardingScreen(onComplete = {})
}

