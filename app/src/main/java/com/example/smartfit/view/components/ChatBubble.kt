package com.example.smartfit.view.components

import android.text.Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartfit.model.ChatMessage
import com.example.smartfit.ui.theme.Black
import java.nio.file.WatchEvent


@Composable
fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (message.isUser)
            Arrangement.End else Arrangement.Start
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd
        ){
            Box(
                modifier = Modifier
                    .background(
                        color = if (message.isUser)
                            Color(0xFFFF7043)   // user bubble
                        else
                            Color(0xFF2B303B),  // friend bubble
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp)
                    .widthIn(max = 260.dp)
            ) {
                Text(
                    text = message.message,
                    color = if (message.isUser) Black else Color(0xFFFFFFFF),
                    fontSize = 14.sp
                )
            }
            Row(
                modifier = Modifier
                    .padding(bottom = 1.dp, end = 10.dp)
            ) {
                Text(
                    text = "12:44",
                    fontSize = 10.sp,
                    modifier = Modifier.padding(end = 2.dp)
                )

            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ChatBubblePreview(){
    ChatBubble(
        ChatMessage("Hello Kartik", true)
    )
}