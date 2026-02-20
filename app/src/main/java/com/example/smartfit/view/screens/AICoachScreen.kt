package com.example.smartfit.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.example.smartfit.model.ChatMessage
import com.example.smartfit.view.components.ChatBubble
import com.example.smartfit.view.components.CustomTextField
import com.example.smartfit.viewModel.ChatViewModel

@Composable
fun AICoachScreen(
    viewModel: ChatViewModel = viewModel()
){

    var userQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column (
                modifier = Modifier.padding(top = 8.dp, start = 16.dp)
            ) {
                Text(
                    text = "AI Fitness Coach",
                    color = Color(0xFFFAFAFA),
                    fontSize = 25.sp
                )
                Text(
                    text = "Get instant answers to your fitness queries",
                    color = Color(0xFFA6A6A6),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                CustomTextField(
                    value = userQuery,
                    onValueChange = {userQuery = it},
                    placeholder = "Type your question",
                    modifier = Modifier.fillMaxWidth(0.85f),
                    borderColor = Color.DarkGray
                )
                Button(
                    onClick = {
                        viewModel.sendMessage(userQuery)
                        userQuery = ""
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(start = 8.dp),
                    contentPadding = PaddingValues(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.send_svgrepo_com),
                        contentDescription = "Send Button",
                        tint = Color(0xFFFAFAFA)
                    )
                }
            }
        },
        containerColor = Color(0xFF0F131A)
    ) {innerPadding->
        Column(
            modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp)
        ) {
            AIChatbot(viewModel)
        }

    }
}

@Composable
fun AIChatbot(
    viewModel: ChatViewModel = viewModel()
){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, Color(0xFF232631), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C202A))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painterResource(R.drawable.robot_svgrepo_com),
                    contentDescription = "Robot Logo",
                    tint = Color(0xFFFF7043),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    "Chat with AI",
                    fontSize = 22.sp,
                    color = Color(0xFFFAFAFA),
                    modifier = Modifier.padding(horizontal = 4.dp))
            }
            //Chat
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .heightIn(min = 400.dp, max = 750.dp)
                    .border(1.dp, Color(0xFF232631))
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp)
            ) {

                viewModel.messages.forEach { msg ->
                    ChatBubble(msg)
                }

                if (viewModel.isLoading) {
                    ChatBubble(ChatMessage("Typing...", false))
                }
            }

            var userQuery by remember { mutableStateOf("") }


        }



    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AICoachScreenPreview(){
    AICoachScreen()
}