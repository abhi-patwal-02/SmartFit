package com.example.smartfit.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                textStyle = TextStyle(color = Color(0xFFFAFAFA), fontSize = fontSize.sp),
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
