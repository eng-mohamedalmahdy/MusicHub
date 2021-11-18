package com.example.musichub.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.musichub.ui.theme.OrangeCrayola

@Composable
fun CircularImage(image: Any?, modifier: Modifier = Modifier, size: Dp = 50.dp) {
    Image(
        painter = rememberImagePainter(image),
        contentDescription = null,
        modifier = modifier
            .padding(8.dp)
            .size(size)
            .clip(CircleShape)
            .background(OrangeCrayola),
        colorFilter = ColorFilter.tint(OrangeCrayola, blendMode = BlendMode.Saturation),
        contentScale = ContentScale.Crop
    )
}
