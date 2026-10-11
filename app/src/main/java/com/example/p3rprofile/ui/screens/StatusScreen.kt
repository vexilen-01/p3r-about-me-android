package com.example.p3rprofile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.p3rprofile.ui.components.p3rSkew
import com.example.p3rprofile.ui.theme.P3RColors

@Composable
fun AboutMeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header matching website
        Text(
            text = "ABOUT ME",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .p3rSkew(-4f)
                .padding(bottom = 8.dp)
        )

        // Card 1: NAME & ROLE
        AboutCard(
            category = "NAME & ROLE",
            title = "Ray / Rayaan",
            subtitle = "Student / Part-Time Programmer"
        )

        // Card 2: EDUCATION
        AboutCard(
            category = "EDUCATION / STUDYING",
            title = "Computer Science Engineering with Artificial Intelligence"
        )

        // Card 3: PERSONALITY PARAMETERS
        AboutCard(
            category = "PERSONALITY PARAMETERS",
            title = "CHILL / STYLISH / CHALANT ASF"
        )

        // Card 4: BIOGRAPHY
        AboutCard(
            category = "BIOGRAPHY",
            title = "Just a guy who loves tech, programming, video games and music."
        )
    }
}

@Composable
fun AboutCard(
    category: String,
    title: String,
    subtitle: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(P3RColors.SiteDarkNavy.copy(alpha = 0.95f))
            .border(width = 1.dp, color = P3RColors.OceanBlue.copy(alpha = 0.2f))
    ) {
        // Pink/Red left accent stripe (4dp wide, full card height)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .background(P3RColors.NeonRedPink)
        )

        Column(modifier = Modifier.padding(start = 10.dp, top = 12.dp, bottom = 12.dp, end = 12.dp)) {
            Text(
                text = category,
                color = P3RColors.ElectricCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}