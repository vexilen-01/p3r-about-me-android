package com.example.p3rprofile.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.p3rprofile.ui.components.p3rSkew
import com.example.p3rprofile.ui.theme.P3RColors

data class PersonalityMeter(val label: String, val value: Int, val max: Int = 100)

@Composable
fun PersonalityScreen() {
    val stats = listOf(
        PersonalityMeter("TECH KNOWLEDGE", 88),
        PersonalityMeter("GENERAL KNOWLEDGE", 68),
        PersonalityMeter("CREATIVITY", 50),
        PersonalityMeter("COURAGE", 60),
        PersonalityMeter("DISCIPLINE", 70),
        PersonalityMeter("GAMING", 95),
        PersonalityMeter("MUSIC", 100),
        PersonalityMeter("CURIOSITY", 75)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "PERSONALITY METERS",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.p3rSkew(-4f)
        )

        stats.forEach { stat ->
            PersonalityMeterBar(stat = stat)
        }
    }
}

@Composable
fun PersonalityMeterBar(stat: PersonalityMeter) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = stat.value.toFloat() / stat.max,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(P3RColors.SiteDarkNavy.copy(alpha = 0.9f))
            .border(1.dp, P3RColors.OceanBlue.copy(alpha = 0.4f))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stat.label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "${stat.value} / ${stat.max}",
                    color = P3RColors.ElectricCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Meter Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(Color(0xFF02060D))
                    .border(1.dp, P3RColors.OceanBlue.copy(alpha = 0.5f))
            ) {
                // Gradient Fill
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = progress.value)
                        .background(
                            Brush.horizontalGradient(
                                listOf(P3RColors.NeonRedPink, P3RColors.OceanBlue, P3RColors.ElectricCyan)
                            )
                        )
                )
            }
        }
    }
}