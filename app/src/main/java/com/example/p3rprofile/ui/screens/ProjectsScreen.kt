package com.example.p3rprofile.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.p3rprofile.ui.components.p3rSkew
import com.example.p3rprofile.ui.theme.P3RColors

@Composable
fun ProjectsScreen() {
    val context = LocalContext.current
    val projectUrl = "https://github.com/vexilen-01/persona-3-reload-about-me-website"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "PROJECTS & MISSIONS",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.p3rSkew(-4f)
        )

        // Project Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(P3RColors.SiteDarkNavy.copy(alpha = 0.95f))
                .border(1.dp, P3RColors.OceanBlue.copy(alpha = 0.4f))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Persona 3 Reload About Me Webpage",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "ACTIVE",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(P3RColors.NeonRedPink.copy(alpha = 0.3f))
                            .border(1.dp, P3RColors.NeonRedPink)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "High-performance interface system inspired by modern JRPG HUD design, liquid particle canvas systems, and dark aesthetic visuals.",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tech tags
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("React", "TypeScript", "Tailwind", "Framer Motion").forEach { tech ->
                        Text(
                            text = tech,
                            color = Color.Gray,
                            fontSize = 10.sp,
                            modifier = Modifier
                                .background(Color(0xFF02060D))
                                .border(1.dp, Color.DarkGray)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Launch Button
                Box(
                    modifier = Modifier
                        .background(P3RColors.NeonRedPink)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(projectUrl))
                            context.startActivity(intent)
                        }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "LAUNCH PROJECT ↗",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}