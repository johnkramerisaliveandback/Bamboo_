package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BambooPrimaryGreen
import com.example.ui.theme.PoppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StartupSplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val word = "BAMBOO"
    val letterAnims = remember { word.map { Animatable(0f) } }
    val overallAlpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        delay(200)
        // Sequential reveal of letters
        word.forEachIndexed { index, _ ->
            launch {
                letterAnims[index].animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 400, easing = EaseOut)
                )
            }
            delay(200) // 200ms delay between each letter start
        }

        // Hold BAMBOO
        delay(800)
        
        // Smooth transition out
        overallAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 400, easing = EaseOut)
        )

        onFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .graphicsLayer { alpha = overallAlpha.value },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            word.forEachIndexed { index, char ->
                Text(
                    text = char.toString(),
                    color = BambooPrimaryGreen,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp,
                    letterSpacing = 4.sp,
                    modifier = Modifier
                        .graphicsLayer { 
                            alpha = letterAnims[index].value
                        }
                        .testTag("startup_letter_$index")
                )
            }
        }
    }
}
