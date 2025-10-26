package com.example.smartfit.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartfit.R

private var signinBtn: Boolean = true
private var signupBtn: Boolean = false

private var mail: String = ""
private var pass: String = ""


@Composable
fun SignupLoginScreen(){
    Column(
        modifier = Modifier
            .background(Color(0xFF0F131A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomSignupLoginCard()
        }

    }
}

@Composable
fun CustomSignupLoginCard(){
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
                text = "Welcome to SmartFit",
                color = Color(0xFFFAFAFA),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier.background(
                    color = Color(0xFF2B303B),
                    shape = RoundedCornerShape(12.dp)
                )
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier.padding(4.dp).fillMaxWidth(0.5f),
                    contentPadding = PaddingValues(vertical = 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(signinBtn) Color(0xFF151820) else Color(0xFF2B303B)),
                    shape = RoundedCornerShape(12.dp)
                    ) {
                    Text("Sign In", fontSize = 12.sp)
                }
                Button(
                    onClick = {},
                    modifier = Modifier.padding(4.dp).fillMaxWidth(1f),
                    contentPadding = PaddingValues(vertical = 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(signinBtn) Color(0xFF2B303B) else Color(0xFF151820)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sign Up", fontSize = 12.sp)
                }
            }
            Column(
            ) {
                Text(
                    text = "Email",
                    color = Color(0xFFFAFAFA),
                    modifier = Modifier.padding(vertical = 8.dp))
                CustomTextField(
                    value = mail,
                    onValueChange = {mail = it},
                    placeholder = "you@example.com"
                )

                Text(
                    text = "Password",
                    color = Color(0xFFFAFAFA),
                    modifier = Modifier.padding(vertical = 8.dp))
                CustomTextField(
                    value = pass,
                    onValueChange = {pass = it},
                    placeholder = "********"
                )
            }

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (signinBtn) "Sign In" else "Sign Up",
                    color = Color(0xFFFAFAFA)
                )
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignupLoginScreenPreview(){
    SignupLoginScreen()
}