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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartfit.R
import com.example.smartfit.view.components.CustomTextField
import com.example.smartfit.viewModel.OnboardingViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OnBoardingScreen(
    onboardingViewModel: OnboardingViewModel = viewModel(),
    onComplete: () -> Unit

) {

    var currWt by remember { mutableStateOf("") }
    var targetWt by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var selectedGoal by remember { mutableStateOf("Lose Weight") }

    // Define save function here
    val saveOnboardingDetails = let@{
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@let
        onboardingViewModel.saveOnboardingDetails(
            uid = uid,
            currWt = currWt,
            targetWt = targetWt,
            height = height,
            goal = selectedGoal,
            onComplete = onComplete // Invoke when save finishes successfully
        )
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
    saveOnboardingDetails: ()->Unit
){
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
                        shape = RoundedCornerShape(50.dp))
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
            }

            Column(
                modifier = Modifier.padding(top = 20.dp).fillMaxWidth()
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
                onClick = saveOnboardingDetails,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text(
                    text = "Complete Setup",
                    color = Color(0xFFFAFAFA),
                    fontSize = 16.sp
                )
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

