package com.example.p3rprofile.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.p3rprofile.ui.theme.P3RColors

enum class P3RTab(val display: String) {
    ABOUT("ABOUT ME"),
    STATS("PERSONALITY"),
    PROJECTS("PROJECTS"),
    GAMES("GAME ARCHIVE"),
    MUSIC("MUSIC"),
    SOCIALS("SOCIAL MEDIA")
}

@Composable
fun P3RNavigationBar(
    currentTab: P3RTab,
    onTabSelected: (P3RTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(P3RColors.SiteDeepBlack)
    ) {
        // Top HUD Status Bar (Direct from your website header)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "LVL ", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = "20", color = P3RColors.ElectricCyan, fontSize = 13.sp, fontWeight = FontWeight.Black)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "DATE ", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = "09.14 / MON", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Horizontal Slanted Navigation Ribbon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            P3RTab.entries.forEach { tab ->
                val isSelected = tab == currentTab

                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.05f else 0.95f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "tabScale"
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .then(
                            if (isSelected) {
                                Modifier
                                    .shadow(elevation = 6.dp, shape = AngledBannerShape(8f), spotColor = P3RColors.ShadowNavy)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(P3RColors.NeonRedPink, P3RColors.HotOrange, P3RColors.NeonRedPink)
                                        ),
                                        shape = AngledBannerShape(8f)
                                    )
                                    .border(1.5.dp, Color.White, shape = AngledBannerShape(8f))
                            } else {
                                Modifier
                                    .background(Color.Transparent)
                                    .border(1.dp, P3RColors.ElectricCyan.copy(alpha = 0.4f), shape = AngledBannerShape(8f))
                            }
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTabSelected(tab) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    // The box edges are already slanted by AngledBannerShape; slant only the label.
                    Text(
                        modifier = Modifier.p3rSkew(-10f),
                        text = tab.display,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isSelected) Color.White else P3RColors.ElectricCyan,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}