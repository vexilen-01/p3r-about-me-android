package com.example.p3rprofile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.example.p3rprofile.ui.theme.P3RColors

/**
 * Loads an image from the internet.
 * While it loads you see a dark navy tile; if it fails (for example offline) you see a "!" tile
 * instead of an empty black box.
 */
@Composable
fun P3RImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
        loading = {
            Box(Modifier.fillMaxSize().background(P3RColors.VelvetIndigo))
        },
        error = {
            Box(
                modifier = Modifier.fillMaxSize().background(P3RColors.ShadowNavy),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "!",
                    color = P3RColors.NeonRedPink,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    )
}
