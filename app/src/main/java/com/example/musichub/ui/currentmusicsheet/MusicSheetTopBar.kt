package com.example.musichub.ui.currentmusicsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musichub.R
import com.example.musichub.data.MusicFile
import com.example.musichub.ui.theme.OrangeCrayola
import com.example.musichub.ui.theme.USAFABlue

@Composable
fun MusicSheetTopBar(
    musicFile: MusicFile,
    onDismissClick: () -> Unit,
    onMenuClickListener: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Image(
            ImageVector.vectorResource(id = R.drawable.ic_dismiss),
            "Dismiss bottom sheet",
            colorFilter = ColorFilter.tint(OrangeCrayola),
            modifier = Modifier
                .padding(all = 12.dp)
                .size(24.dp)
                .border(width = 3.dp, color = OrangeCrayola, shape = CircleShape)
                .clip(CircleShape)
                .clickable { onDismissClick() }

        )
        MusicItemDetails(musicFile)
        Image(
            ImageVector.vectorResource(id = R.drawable.ic_more),
            "Dismiss bottom sheet",
            colorFilter = ColorFilter.tint(OrangeCrayola),
            modifier = Modifier
                .padding(all = 12.dp)
                .size(24.dp)
                .border(width = 3.dp, color = OrangeCrayola, shape = CircleShape)
                .clip(CircleShape)
                .clickable { onMenuClickListener() }

        )
    }
}

@Composable
private fun MusicItemDetails(musicFile: MusicFile) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = musicFile.getProperName(), color = USAFABlue, fontSize = 20.sp
        )
        Row {
            Text(
                text = musicFile.artist, color = USAFABlue, fontSize = 10.sp
            )
            Text(
                text = musicFile.album, color = USAFABlue, fontSize = 10.sp
            )
        }
    }
}