package com.example.musichub.ui.currentmusicsheet


import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.musichub.R
import com.example.musichub.ui.theme.OrangeCrayola


@Composable
fun MusicItemImage(image: ByteArray?) {


    val bmp by remember {
        mutableStateOf(
            BitmapFactory.decodeByteArray(image, 0, image?.size ?: 0)
                ?: R.drawable.ic_baseline_music_note_24
        )
    }
    Image(

        painter = rememberImagePainter(bmp),
        contentDescription = null,
        modifier = Modifier
            .size(250.dp)
            .clip(CircleShape)                       // clip to the circle shape
            .border(12.dp, OrangeCrayola, CircleShape)
            .background(OrangeCrayola)
    )
}