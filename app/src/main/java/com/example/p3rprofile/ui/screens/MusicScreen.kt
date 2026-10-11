package com.example.p3rprofile.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import com.example.p3rprofile.audio.MusicController
import com.example.p3rprofile.ui.components.P3RCard
import com.example.p3rprofile.ui.components.P3RSectionTitle
import com.example.p3rprofile.ui.theme.P3RColors

data class Artist(val name: String, val imgUrl: String)

@Composable
fun MusicScreen() {
    val currentTrack = MusicController.currentTrack
    val isPlaying = MusicController.isPlaying
    val trackIndex = MusicController.currentTrackIndex
    val totalTracks = MusicController.playlist.size
    val isLoading = MusicController.isLoading
    val hasError = MusicController.hasError

    // Poll the player twice a second to move the progress bar.
    var positionMs by remember { mutableIntStateOf(0) }
    var durationMs by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            positionMs = MusicController.positionMs()
            durationMs = MusicController.durationMs()
            delay(500)
        }
    }

    val artists = listOf(
        Artist("Joji", "https://persona-3-reload-about-me-website-beta.vercel.app/joji.jpg"),
        Artist("aespa", "https://persona-3-reload-about-me-website-beta.vercel.app/aespa.jpg"),
        Artist("Tory Lanez", "https://persona-3-reload-about-me-website-beta.vercel.app/tory-lanez.jpg"),
        Artist("Deftones", "https://persona-3-reload-about-me-website-beta.vercel.app/deftones.jpg"),
        Artist("Slipknot", "https://persona-3-reload-about-me-website-beta.vercel.app/slipknot.jpg")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        P3RSectionTitle(
            text = "MUSIC CORNER",
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- P3R PORTABLE MP3 PLAYER CONTAINER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            // White Earphone Cable Canvas Overlay (Curving across the player)
            Canvas(modifier = Modifier.matchParentSize().padding(horizontal = 4.dp)) {
                val cordPath = Path().apply {
                    moveTo(size.width * 0.12f, size.height * 0.5f)
                    cubicTo(
                        size.width * 0.12f, size.height * 1.05f,
                        size.width * 0.45f, size.height * 1.02f,
                        size.width * 0.88f, size.height * 0.65f
                    )
                    cubicTo(
                        size.width * 0.95f, size.height * 0.25f,
                        size.width * 0.80f, -size.height * 0.15f,
                        size.width * 0.65f, 0f
                    )
                }
                drawPath(
                    path = cordPath,
                    color = Color.White.copy(alpha = 0.95f),
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // The Metallic Pill Player Body
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { rotationZ = -1.5f }
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(50.dp),
                        spotColor = P3RColors.ElectricCyan,
                        ambientColor = P3RColors.ElectricCyan
                    )
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(P3RColors.PlayerShellLight, Color(0xFFFFFFFF), P3RColors.PlayerShellBlue)
                        )
                    )
                    .border(2.5.dp, Color.White, RoundedCornerShape(50.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Plug Socket
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(P3RColors.PlayerSteel)
                            .border(2.dp, P3RColors.PlayerSteelDark, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Album cover fills the disc; the ring and dot on top make it look like a CD.
                        AsyncImage(
                            model = currentTrack.coverUrl,
                            contentDescription = currentTrack.album,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, P3RColors.PlayerSteel, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(P3RColors.ElectricCyan)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // LCD Screen
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(P3RColors.PlayerScreen)
                            .border(1.dp, P3RColors.ElectricCyan.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = currentTrack.album,
                                    color = P3RColors.ElectricCyan,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                Text(
                                    text = "[${trackIndex + 1}/$totalTracks]",
                                    color = P3RColors.ElectricCyan,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = currentTrack.title,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentTrack.artist,
                                    color = P3RColors.ElectricCyan.copy(alpha = 0.85f),
                                    fontSize = 9.sp,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = when {
                                        hasError -> "OFFLINE ▶"
                                        isLoading -> "LOADING…"
                                        isPlaying -> "❚❚ PAUSE"
                                        else -> "▶ PLAY"
                                    },
                                    color = P3RColors.ElectricCyan,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            // Track progress bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .background(P3RColors.ElectricCyan.copy(alpha = 0.2f))
                            ) {
                                val progress = if (durationMs > 0) {
                                    (positionMs.toFloat() / durationMs).coerceIn(0f, 1f)
                                } else 0f
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(fraction = progress)
                                        .background(P3RColors.ElectricCyan)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Controls (Prev, Play/Pause circle, Next)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Previous
                        Text(
                            text = "◁❚",
                            color = P3RColors.PlayerIconGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier
                                .clickable {
                                    MusicController.prevTrack()
                                }
                                .padding(4.dp)
                        )

                        // Main Circular Play Button
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isPlaying) Brush.radialGradient(listOf(P3RColors.ElectricCyan, P3RColors.OceanBlue))
                                    else Brush.radialGradient(listOf(P3RColors.PlayerButtonLight, P3RColors.PlayerSteelDark))
                                )
                                .border(1.5.dp, Color.White, CircleShape)
                                .clickable {
                                    MusicController.togglePlay()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isPlaying) "❚❚" else "▶",
                                color = if (isPlaying) Color.White else P3RColors.PlayerIconDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Next
                        Text(
                            text = "❚▷",
                            color = P3RColors.PlayerIconGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier
                                .clickable {
                                    MusicController.nextTrack()
                                }
                                .padding(4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // --- FAVORITE ARTISTS CARD DECK ---
        P3RCard(
            background = P3RColors.SiteDeepBlack,
            backgroundAlpha = 0.85f,
            borderAlpha = 0.5f,
            contentPadding = 14.dp
        ) {
            Column {
                Text(
                    text = "FAVORITE ARTISTS",
                    color = P3RColors.ElectricCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    artists.forEach { artist ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .background(P3RColors.MidnightBlack)
                                .border(1.dp, P3RColors.OceanBlue.copy(alpha = 0.2f))
                                .padding(vertical = 10.dp, horizontal = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, P3RColors.ElectricCyan, CircleShape)
                            ) {
                                AsyncImage(
                                    model = artist.imgUrl,
                                    contentDescription = artist.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = artist.name,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}