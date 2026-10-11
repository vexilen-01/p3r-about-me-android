package com.example.p3rprofile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.p3rprofile.ui.components.P3RCard
import com.example.p3rprofile.ui.components.P3RImage
import com.example.p3rprofile.ui.theme.P3RColors

data class GameEntry(
    val title: String,
    val rating: String,
    val genre: String,
    val platform: String,
    val note: String,
    val imgUrl: String
)

@Composable
fun GamesScreen() {
    val games = listOf(
        GameEntry(
            title = "Persona 3 Reload",
            rating = "10 / 10",
            genre = "JRPG / Adventure / Strategy",
            platform = "PC / Console",
            note = "The primary design inspiration for this entire visual interface. Flawless UI presentation, visual density, and soundtrack composition.",
            imgUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/p3r.jpg"
        ),
        GameEntry(
            title = "Devil May Cry",
            rating = "9.5 / 10",
            genre = "Hack and Slash / Action-Adventure",
            platform = "PS2 / PS3 / PC",
            note = "One of my favourite games despite being a very old game another game with amazing story with insane combat experience.",
            imgUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/dmc.jpg"
        ),
        GameEntry(
            title = "Valorant",
            rating = "7.8 / 10",
            genre = "Tactical Shooter / FPS",
            platform = "PC",
            note = "Made me lose some of my sanity but the game overall is fine xD.",
            imgUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/valorant.jpg"
        ),
        GameEntry(
            title = "Marvel Rivals",
            rating = "8.8 / 10",
            genre = "Hero Shooter / Action",
            platform = "PC",
            note = "Another game which makes me lose my mind but I somehow manage </3.",
            imgUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/marvel-rivals.jpg"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        games.forEach { game ->
            P3RCard(background = P3RColors.GameCardNavy, borderAlpha = 0.5f) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Game Cover Box
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Black)
                            .border(1.dp, P3RColors.ElectricCyan, RoundedCornerShape(4.dp))
                    ) {
                        P3RImage(
                            url = game.imgUrl,
                            contentDescription = game.title,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Details Column
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = game.title,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )

                            // Rating Pill
                            Text(
                                text = game.rating,
                                color = P3RColors.ElectricCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier
                                    .background(P3RColors.OceanBlue.copy(alpha = 0.25f))
                                    .border(1.dp, P3RColors.ElectricCyan.copy(alpha = 0.6f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "GENRE: ${game.genre}",
                            color = Color.Gray,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "PLATFORM: ${game.platform}",
                            color = Color.Gray,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = game.note,
                            color = P3RColors.NoteGray,
                            fontSize = 11.sp,
                            fontStyle = FontStyle.Italic,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}