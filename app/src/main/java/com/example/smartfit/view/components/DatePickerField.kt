package com.example.smartfit.view.components

import android.app.DatePickerDialog
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.util.Calendar
import java.util.Locale

@Composable
fun DatePickerField(
    label: String = "dd-mm-yyyy",
    date: String,
    onDateSelected: (String) -> Unit
) {

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val dialog = remember {
        DatePickerDialog(
            context,
            { _, y, m, d ->
                val formatted = String.format(
                    Locale.getDefault(),
                    "%04d-%02d-%02d",
                    y,
                    m + 1,
                    d
                )
                onDateSelected(formatted)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }
    }

    // Limit to past dates only
    dialog.datePicker.maxDate = System.currentTimeMillis()

    CustomTextField(
        value = date,
        onValueChange = {},
        placeholder = label,
        modifier = Modifier
            .fillMaxWidth(),
        readOnly = true,
        onClick = {dialog.show()}
    )
}

@Composable
fun DobPickerField(
    date: String,
    onDateSelected: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    CustomTextField(
        value = date,
        onValueChange = {},
        placeholder = "dd-mm-yyyy",
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        onClick = { showDialog = true }
    )

    if (showDialog) {

        val context = LocalContext.current

        AndroidView(
            factory = {

                val calendar = Calendar.getInstance()

                DatePickerDialog(
                    context,
                    { _, y, m, d ->

                        val formatted = String.format(
                            Locale.getDefault(),
                            "%04d-%02d-%02d",
                            y,
                            m + 1,
                            d
                        )

                        onDateSelected(formatted)
                        showDialog = false
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).apply {
                    datePicker.maxDate = System.currentTimeMillis()
                    show()
                }

                View(context) // required dummy view
            }
        )
    }
}