package com.example.p3rprofile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.p3rprofile.ui.theme.P3RColors

/**
 * The dark navy panel with a thin blue border used on every screen.
 * Pass [onClick] to make the whole card tappable.
 */
@Composable
fun P3RCard(
    modifier: Modifier = Modifier,
    background: Color = P3RColors.SiteDarkNavy,
    backgroundAlpha: Float = 0.9f,
    borderAlpha: Float = 0.4f,
    contentPadding: Dp = 12.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(background.copy(alpha = backgroundAlpha))
            .border(1.dp, P3RColors.OceanBlue.copy(alpha = borderAlpha))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(contentPadding)
    ) {
        content()
    }
}

/** Big italic, slanted page heading ("ABOUT ME", "MUSIC CORNER", ...). */
@Composable
fun P3RSectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 30.sp
) {
    Text(
        text = text,
        color = Color.White,
        fontSize = fontSize,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        modifier = modifier.p3rSkew(-4f)
    )
}
