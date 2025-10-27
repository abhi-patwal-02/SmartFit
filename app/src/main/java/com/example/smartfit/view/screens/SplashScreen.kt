package com.example.smartfit.view.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.smartfit.R

@Composable
fun SplashScreen() {
    Column(modifier = Modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(){
                Image(
                    painter = painterResource(R.drawable.hero_fitness),
                    contentDescription = "Background",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier.fillMaxSize().background(color = Color(0xEB0F131A)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GlowingLogo()
                    Text(
                        text = "FitTrack Pro",
                        fontSize = 30.sp,
                        color = Color(0xFFFF7043),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    AnimatedDots()
                }
            }

        }

    }
}

@Composable
fun AnimatedDots(
    dotSize: Dp = 8.dp,
    color: Color = Color(0xFFFF7043),
    distance: Dp = 10.dp,
    duration: Int = 600
) {
    val transition = rememberInfiniteTransition()

    val yOffsets = List(3) { i ->
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 0.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, easing = LinearEasing, delayMillis = i * 100),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(dotSize),
        verticalAlignment = Alignment.CenterVertically
    ) {
        yOffsets.forEach { animatedOffset ->
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .offset(y = (-animatedOffset.value * distance))
                    .background(color, shape = CircleShape)
            )
        }
    }
}

@Composable
fun GlowingLogo() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
        // Simulate glow with a large translucent circle
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF7043).copy(alpha = 0.8f),
                            Color(0xFFFF7043).copy(alpha = 0.7f),
                            Color(0xFFFF7043).copy(alpha = 0.4f),
                            Color(0xFFFF7043).copy(alpha = 0.8f),
                            Color(0xFFFF7043).copy(alpha = 0.8f),
                            Color(0xFFFF7043).copy(alpha = 0.8f),
                            Color(0xFFFF7043).copy(alpha = 0.8f),
                            Color(0xFFFF7043).copy(alpha = 0.8f),
                            Color(0xFFFF7043).copy(alpha = 0.8f),
                            Color(0xFFFF7043).copy(alpha = 0.2f),
                            Color(0xFFFF7043).copy(alpha = 0.4f),
                            Color(0xFFFF7043).copy(alpha = 0.2f),
                            Color(0xFFFF7043).copy(alpha = 0.08f),
                            Color(0xFFFF7043).copy(alpha = 0.05f),
                            Color(0xFFFF7043).copy(alpha = 0.02f),
                            Color(0xFFFF7043).copy(alpha = 0.01f),
                        )
                    ),
                    shape = CircleShape
                )
        )
        // Logo circle
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = Color(0xFFFF7043),
                    shape = CircleShape
                )
        ) {
            Icon(
                painter = painterResource(R.drawable.dumbell_svgrepo_com),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(44.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview(){
    SplashScreen()
}

