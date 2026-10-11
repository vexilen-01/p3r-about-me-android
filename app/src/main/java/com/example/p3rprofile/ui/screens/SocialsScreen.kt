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
import com.example.p3rprofile.ui.components.P3RCard
import com.example.p3rprofile.ui.components.P3RSectionTitle
import com.example.p3rprofile.ui.theme.P3RColors

data class SocialItem(val name: String, val category: String, val desc: String, val link: String)

@Composable
fun SocialsScreen() {
    val context = LocalContext.current
    val socials = listOf(
        SocialItem("GitHub Profile", "COLLABORATION", "Code repositories and software projects.", "https://github.com/vexilen-01"),
        SocialItem("Instagram", "COMMUNITY", "Personal social updates and visuals.", "https://www.instagram.com/vexilen_/"),
        SocialItem("Reddit", "COMMUNITY", "Gaming, tech communities, and discussions.", "https://www.reddit.com/user/Swimming-Round-1963/")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        P3RSectionTitle("SOCIAL MEDIA")

        socials.forEach { item ->
            P3RCard(
                contentPadding = 14.dp,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                    context.startActivity(intent)
                }
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = item.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = "LINKED ►", color = P3RColors.ElectricCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = item.category, color = Color.Gray, fontSize = 10.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = item.desc, color = Color.LightGray, fontSize = 12.sp)
                }
            }
        }
    }
}