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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.smartfit.ui.theme.DDBlue
import com.example.smartfit.ui.theme.Grey
import com.example.smartfit.ui.theme.LGrey
import com.example.smartfit.ui.theme.Orange
import com.example.smartfit.ui.theme.WText
import com.example.smartfit.view.components.CustomTextField
import com.example.smartfit.viewModel.AuthViewModel



@Composable
fun SignupLoginScreen(authViewModel: AuthViewModel?){
    var signinBtn by remember { mutableStateOf(false) }
    var mail by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .background(DDBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomSignupLoginCard(
                name = name,
                onNameChange = {name = it},
                signinBtn = signinBtn,
                onToggleSignInSignUp = { signinBtn = !signinBtn },
                mail = mail,
                onMailChange = { mail = it },
                pass = pass,
                onPassChange = { pass = it },
                onButtonClick = {
                    if (signinBtn) {
                        authViewModel?.login(mail.trim(), pass)
                    } else {
                        authViewModel?.signUp(mail.trim(), pass, name.trim())
                    }
                }
            )
        }
    }
}

@Composable
fun CustomSignupLoginCard(
    name: String,
    onNameChange:(String)->Unit,
    signinBtn: Boolean,
    onToggleSignInSignUp: () -> Unit,
    mail: String,
    onMailChange: (String) -> Unit,
    pass: String,
    onPassChange: (String) -> Unit,
    onButtonClick: () -> Unit,
){
    Card(
        colors = CardDefaults.cardColors(containerColor = Grey)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = Orange,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.dumbell_svgrepo_com),
                    contentDescription = "logo",
                    tint = WText,
                    modifier = Modifier.size(30.dp)
                )
            }
            Text(
                text = "Welcome to SmartFit",
                color = WText,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier.background(
                    color = LGrey,
                    shape = RoundedCornerShape(12.dp)
                )
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = onToggleSignInSignUp,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(0.5f),
                    contentPadding = PaddingValues(vertical = 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(signinBtn) DDBlue else LGrey),
                    shape = RoundedCornerShape(12.dp)
                    ) {
                    Text("Sign In", fontSize = 12.sp, color = WText)
                }
                Button(
                    onClick = onToggleSignInSignUp,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(1f),
                    contentPadding = PaddingValues(vertical = 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(signinBtn) LGrey else DDBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sign Up", fontSize = 12.sp, color = WText)
                }
            }
            Column(
            ) {
                if (!signinBtn) {
                    Text(
                        text = "Name",
                        color = WText,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    CustomTextField(
                        value = name,
                        onValueChange = onNameChange,
                        placeholder = "Your Name"
                    )
                }
                Text(
                    text = "Email",
                    color = WText,
                    modifier = Modifier.padding(vertical = 8.dp))
                CustomTextField(
                    value = mail,
                    onValueChange = onMailChange,
                    placeholder = "you@example.com"
                )

                Text(
                    text = "Password",
                    color = WText,
                    modifier = Modifier.padding(vertical = 8.dp))
                CustomTextField(
                    value = pass,
                    onValueChange = onPassChange,
                    placeholder = "********"
                )
            }

            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Orange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (signinBtn) "Sign In" else "Sign Up",
                    color = WText
                )
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignupLoginScreenPreview(){
    SignupLoginScreen(authViewModel = null)
}