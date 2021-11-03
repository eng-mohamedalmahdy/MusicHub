package com.example.musichub.ui.musiclist

import android.view.RoundedCorner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MusicList(musicListViewModel: MusicListViewModel) {
    val musicList = musicListViewModel.getAllMusic()
    var currentMusic by remember { mutableStateOf(0) }
    LazyColumn(
        modifier = Modifier
            .background(Beige)
            .fillMaxHeight(.92f)
    ) {
        itemsIndexed(musicList) { idx, musicFile ->
            MusicRow(idx, musicFile)
            Divider(
                Modifier
                    .height(.5.dp)
                    .background(Color.Black)
            )
        }
    }
    MusicControl(musicList[currentMusic])
}

@Composable
fun MusicControl(currentMusicFile: MusicFile) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(USAFABlue)
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val image = R.drawable.ic_baseline_music_note_24 ?: R.drawable.ic_baseline_music_note_24

        Image(
            painter = rememberImagePainter(builder = {

            }, data = image),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape),
            colorFilter = ColorFilter.tint(OrangeCrayola, blendMode = BlendMode.Saturation),

            )
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = currentMusicFile.title, color = Color.Black, fontSize = 20.sp
                )
                Text(
                    text = "Length: ${currentMusicFile.getFormattedDuration()}",
                    color = Color.Black,
                    fontSize = 17.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(.4f)
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

@ExperimentalCoilApi
@Composable
fun MusicRow(
    idx: Int,
    musicFile: MusicFile
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 8.dp)
    ) {
        val image = R.drawable.ic_baseline_music_note_24 ?: R.drawable.ic_baseline_music_note_24

        if (idx != -1) {
            Text(
                text = (idx + 1).toString(),
                color = Color.Black,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(.05f)
            )
        }
        Image(
            painter = rememberImagePainter(builder = {

            }, data = image),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape),
            colorFilter = ColorFilter.tint(OrangeCrayola, blendMode = BlendMode.Saturation),

            )
        Column {
            Text(
                text = musicFile.title, color = Color.Black, fontSize = 20.sp
            )
            Row {
                Text(
                    text = musicFile.artist, color = Color.Black, fontSize = 10.sp
                )
                Text(
                    text = musicFile.album, color = Color.Black, fontSize = 10.sp
                )
            }
        }
        Text(
            text = musicFile.getFormattedDuration(),
            color = Color.Black,
            fontSize = 10.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
        )


    }
}