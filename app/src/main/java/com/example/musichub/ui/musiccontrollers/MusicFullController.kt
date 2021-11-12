package com.example.musichub.ui.musiccontrollers

import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musichub.data.PlayingRepeatStatus
import com.example.musichub.ui.theme.USAFABlue

@Composable
fun MusicFullController(
    isPlaying: Boolean,
    playingRepeatStatus: PlayingRepeatStatus,
    sliderStartText: String,
    sliderEndText: String,
    sliderValue: Float,
    onSliderMove: (percent: Float) -> Unit,
    onPlayClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onRepeatClick: () -> Unit,
) {
    Column {
        MusicFileSlider(
            sliderStartText = sliderStartText,
            sliderEndText = sliderEndText,
            sliderValue = sliderValue,
            onSliderMove = onSliderMove
        )
        MusicControlButtons(
            isPlaying,
            playingRepeatStatus,
            onRepeatClick,
            onPreviousClick,
            onPlayClick,
            onNextClick
        )

    }

}

@Composable
private fun MusicControlButtons(
    isPlaying: Boolean,
    repeatStatus: PlayingRepeatStatus,
    onRepeatClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onPlayClick: () -> Unit,
    onNextClick: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        RepeatingButton(repeatStatus, onRepeatClick)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            PreviousButton(onClick = onPreviousClick)
            PlayButton(isPlaying = isPlaying, onClick = onPlayClick)
            NextButton(onClick = onNextClick)

        }
    }
}


@Composable
private fun MusicFileSlider(
    sliderEndText: String,
    sliderStartText: String,
    sliderValue: Float,
    onSliderMove: (percent: Float) -> Unit
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        ) {
            Text(text = sliderStartText, color = USAFABlue)
            Text(text = sliderEndText, color = USAFABlue)
        }
        Slider(
            value = sliderValue,
            onValueChange = onSliderMove,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
        )

    }

}
