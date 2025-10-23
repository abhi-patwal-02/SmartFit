package com.example.smartfit.view.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartfit.R

@Composable
fun ExerciseScreen(){

    Column(modifier = Modifier
        .background(Color(0xFF0F131A))
        .fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start){

            Text(
                text = "Workout Tracker",
                fontSize = 25.sp,
                color = Color(0xFFFAFAFA)
            )

            Text(
                text = "Log your exercises and track progress",
                color = Color(0xFFA6A6A6)
            )

            TodayProgressBar(1,3)

            GoTo3DModel()

            AddExercise()

            ExerciseLogs()
        }
    }

}

@Composable
fun ExerciseLogs(){

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C202A))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Today's Workouts", fontSize = 25.sp, color = Color(0xFFFAFAFA))

        }
    }
}

@Composable
fun AddExercise() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(Color(0xFF1C202A))
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text("Add Exercise", fontSize = 25.sp, color = Color(0xFFFAFAFA))

            var exName by remember { mutableStateOf("") } //ExerciseName
            var setNum by remember { mutableStateOf("") } //Number of Sets
            var repNum by remember { mutableStateOf("") } //Number of Reps
            var wtQuantity by remember { mutableStateOf("") } // Amount of Weight for the exercise.

            CustomTextField(
                value = exName,
                onValueChange = {exName = it},
                placeholder = "Exercise Name"
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth(0.33f)
                    .padding(vertical = 8.dp)
                    .padding(end = 4.dp)
                ) {
                    Text("Sets",
                        fontSize = 10.sp,
                        color = Color(0xFFA6A6A6),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    CustomTextField(
                        value = setNum,
                        onValueChange = {setNum = it},
                        placeholder = "0"
                    )
                }

                Column(modifier = Modifier.fillMaxWidth(0.5f)
                    .padding(vertical = 8.dp)
                    .padding(end = 4.dp)
                ) {
                    Text("Reps",
                        fontSize = 10.sp,
                        color = Color(0xFFA6A6A6),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    CustomTextField(
                        value = repNum,
                        onValueChange = {repNum = it},
                        placeholder = "0"
                    )
                }

                Column(modifier = Modifier.fillMaxWidth(1f)
                    .padding(vertical = 8.dp)
                ) {
                    Text("Weight",
                        fontSize = 10.sp,
                        color = Color(0xFFA6A6A6),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    CustomTextField(
                        value = wtQuantity,
                        onValueChange = {wtQuantity = it},
                        placeholder = "0"
                    )
                }
            }

            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFB5936))) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Icon(painter = painterResource(R.drawable.outline_add_24),
                        contentDescription = "Add Image",
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xFFFAFAFA)
                    )
                    Text("Add Exercise")
                }

            }
        }
    }
}


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
        .border(
            width = 2.dp,
            color = Color(0xFF232631), // Light gray border, change as needed
            shape = RoundedCornerShape(8.dp)
        ),
    image: Painter? = null,
    placeholder: String = "",
    backgroundColor: Color = Color(0xFF151820),
    borderColor: Color = Color.Transparent,
    cornerRadius: Int = 6,
    fontSize: Int = 16,
    padding: PaddingValues = PaddingValues(horizontal = 12.dp)
) {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(cornerRadius.dp))
                .border(1.dp, borderColor, RoundedCornerShape(cornerRadius.dp))
                .height(40.dp)
                .padding(padding),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (image != null) {
                    androidx.compose.foundation.Image(
                        painter = image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp)
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(color = Color.Black, fontSize = fontSize.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = Color(0xFFA6A6A6),
                                fontSize = fontSize.sp
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }
}

@Composable
fun GoTo3DModel(){
    Card(modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFF1C202A))) {
        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
            Column {
                Text("View 3D Anatomy Model", fontSize = 22.sp, color = Color(0xFFFAFAFA))
                Text("Learn Correct Form for any Exercise.", color = Color(0xFFA6A6A6))
            }
            Image(
                painter = painterResource(R.drawable.outline_play_arrow_24),
                contentDescription = "Go to 3D model.",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
@Composable
fun TodayProgressBar(value: Int, maxValue: Int){
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFF00BFFF))
    ) {
        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
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
    arcColor: Color = Color.White,
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
    ExerciseScreen()
}