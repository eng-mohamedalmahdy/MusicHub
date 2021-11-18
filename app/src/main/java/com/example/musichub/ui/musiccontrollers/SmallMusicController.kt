package com.example.musichub.ui.musiccontrollers

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.musichub.R
import com.example.musichub.data.MusicFile
import com.example.musichub.ui.CircularImage
import com.example.musichub.ui.previewproviders.SampleMusicFileProvider
import com.example.musichub.ui.theme.OrangeCrayola
import com.example.musichub.ui.theme.SapphireBlue

@Preview
@Composable
fun SmallMusicController(
    @PreviewParameter(SampleMusicFileProvider::class) currentMusicFile: MusicFile,
    isPlaying: Boolean = true,
    onContainerClick: () -> Unit = {},
    onPlayClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .background(SapphireBlue)
            .sizeIn(minHeight = 50.dp)
            .fillMaxWidth()
            .clickable { onContainerClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        SongDetails(currentMusicFile)
        PlayButton(isPlaying, onPlayClick)
        NextButton(onNextClick)
    }
}

@Composable
private fun SongDetails(currentMusicFile: MusicFile) {

    val imageByteArray by remember { mutableStateOf(currentMusicFile.getImage()) }

    val image by remember {
        mutableStateOf(
            BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
                ?: R.drawable.ic_baseline_music_note_24
        )
    }
    Row(Modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp)) {
        CircularImage(image = image)
        Column(Modifier.padding(start = 4.dp, top = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = currentMusicFile.getProperName(), color = Color.Black, fontSize = 20.sp
                )
                Text(
                    text = "Length: ${currentMusicFile.getFormattedDuration()}",
                    color = Color.Black,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row {
                Text(
                    text = currentMusicFile.artist, color = Color.Black, fontSize = 10.sp
                )
                Text(
                    text = currentMusicFile.album, color = Color.Black, fontSize = 10.sp
                )
            }
        }
    }
}


