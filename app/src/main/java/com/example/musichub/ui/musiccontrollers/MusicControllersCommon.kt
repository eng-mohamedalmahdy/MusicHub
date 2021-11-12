package com.example.musichub.ui.musiccontrollers

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.musichub.R
import com.example.musichub.data.PlayingRepeatStatus
import com.example.musichub.ui.theme.OrangeCrayola
import com.example.musichub.ui.theme.PeachCrayola

@Composable
internal fun PlayButton(isPlaying: Boolean, onClick: () -> Unit) {
    val imageId = if (!isPlaying) R.drawable.ic_play else R.drawable.ic_pause

    Image(
        ImageVector.vectorResource(id = imageId),
        "Localized description",
        colorFilter = ColorFilter.tint(OrangeCrayola),
        modifier = Modifier
            .padding(start = 24.dp)
            .size(50.dp)
            .border(2.dp, color = PeachCrayola, shape = CircleShape)
            .border(3.dp, color = PeachCrayola, shape = CircleShape)
            .padding(4.dp)
            .clip(CircleShape)
            .clickable { onClick() }

    )

}

@Composable
internal fun NextButton(onClick: () -> Unit) {
    Image(
        ImageVector.vectorResource(id = R.drawable.ic_skip_next),
        "Localized description",
        colorFilter = ColorFilter.tint(OrangeCrayola),
        modifier = Modifier
            .padding(start = 24.dp)
            .size(30.dp)
            .border(3.dp, color = PeachCrayola, shape = CircleShape)
            .padding(4.dp)
            .clip(CircleShape)
            .clickable { onClick() }

    )

}

@Composable
internal fun PreviousButton(onClick: () -> Unit) {
    Image(
        ImageVector.vectorResource(id = R.drawable.ic_previous),
        "Localized description",
        colorFilter = ColorFilter.tint(OrangeCrayola),
        modifier = Modifier
            .padding(start = 24.dp)
            .size(30.dp)
            .border(3.dp, color = PeachCrayola, shape = CircleShape)
            .padding(4.dp)
            .clip(CircleShape)
            .clickable { onClick() }

    )

}

@Composable
internal fun RepeatingButton(repeatStatus: PlayingRepeatStatus, onClick: () -> Unit) {

    val image = when (repeatStatus) {
        is PlayingRepeatStatus.REPEAT_ALL -> R.drawable.ic_repeat_all
        PlayingRepeatStatus.REPEAT_ONE -> R.drawable.ic_repeat_one
        PlayingRepeatStatus.SHUFFLE -> R.drawable.ic_shuffle
    }
    Image(
        ImageVector.vectorResource(id = image),
        "Localized description",
        colorFilter = ColorFilter.tint(OrangeCrayola),
        modifier = Modifier
            .padding(start = 24.dp)
            .size(35.dp)
            .border(3.dp, color = PeachCrayola, shape = CircleShape)
            .padding(4.dp)
            .clip(CircleShape)
            .clickable { onClick() }

    )

}
