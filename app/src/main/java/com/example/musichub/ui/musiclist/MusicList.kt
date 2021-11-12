package com.example.musichub.ui.musiclist

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.musichub.R
import com.example.musichub.data.MusicFile
import com.example.musichub.ui.theme.*

@Composable
fun MusicList(musicList: List<MusicFile>, onItemClick: (idx: MusicFile) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .background(Beige)
            .fillMaxHeight(.92f)
    ) {
        itemsIndexed(musicList) { idx, musicFile ->
            MusicListRow(idx, musicFile, onItemClick = onItemClick)

            Divider(
                Modifier
                    .height(.5.dp)
                    .background(Color.Black)
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
private fun MusicListRow(
    idx: Int,
    musicFile: MusicFile,
    onItemClick: (idx: MusicFile) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
            .clickable { onItemClick(musicFile) }
    ) {

        val index by remember { mutableStateOf(idx) }
        val title by remember { mutableStateOf(musicFile.title) }
        val artist by remember { mutableStateOf(musicFile.artist) }
        val album by remember { mutableStateOf(musicFile.album) }
        val duration by remember { mutableStateOf(musicFile.getFormattedDuration()) }
        val imageByteArray by remember { mutableStateOf(musicFile.getImage()) }

        val image by remember {
            mutableStateOf(
                BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
                    ?: R.drawable.ic_baseline_music_note_24
            )
        }


        Text(
            text = (index + 1).toString(),
            color = Color.Black,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth(.075f)
        )

        Image(
            painter = rememberImagePainter(image),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(OrangeCrayola),
            colorFilter = ColorFilter.tint(OrangeCrayola, blendMode = BlendMode.Saturation),

            )
        Column {
            Text(
                text = title, color = Color.Black, fontSize = 20.sp
            )
            Row {
                Text(
                    text = artist, color = Color.Black, fontSize = 10.sp
                )
                Text(
                    text = album, color = Color.Black, fontSize = 10.sp
                )
            }
        }
        Text(
            text = duration,
            color = Color.Black,
            fontSize = 10.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
        )
    }
}