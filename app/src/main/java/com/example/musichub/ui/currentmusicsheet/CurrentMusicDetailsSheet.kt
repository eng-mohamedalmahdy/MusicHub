package com.example.musichub.ui.currentmusicsheet


import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musichub.R
import com.example.musichub.data.MusicFile
import com.example.musichub.ui.CircularImage
import com.example.musichub.ui.musiccontrollers.MusicFullController
import com.example.musichub.ui.musicpage.MusicListViewModel
import com.example.musichub.ui.theme.Beige
import com.example.musichub.ui.theme.Shapes
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Composable
fun CurrentMusicDetailsSheet(
    musicFile: MusicFile,
    isShowingDetails: ModalBottomSheetState,
    viewModel: MusicListViewModel,
    content: @Composable () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val isPlaying = viewModel.playingStatus.collectAsState()
    val repeatStatus = viewModel.repeatingStatus.collectAsState()
    val musicSliderStartText = viewModel.currentMusicFormattedFileStamp.collectAsState()
    val musicSliderValue = viewModel.sliderPercent.collectAsState()
    val bmp by remember {
        mutableStateOf(
            BitmapFactory.decodeByteArray(
                musicFile.getImage(),
                0,
                musicFile.getImage().size
            )
                ?: R.drawable.ic_baseline_music_note_24
        )
    }

    ModalBottomSheetLayout(
        content = content,
        sheetState = isShowingDetails,
        sheetShape = Shapes.large,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(Beige),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                MusicSheetTopBar(
                    musicFile = musicFile,
                    onDismissClick = { coroutineScope.launch { isShowingDetails.hide() } },
                    onMenuClickListener = {})

                CircularImage(image = bmp, size = 250.dp)

                MusicFullController(
                    isPlaying = isPlaying.value,
                    playingRepeatStatus = repeatStatus.value,
                    sliderStartText = musicSliderStartText.value,
                    sliderEndText = musicFile.getFormattedDuration(),
                    sliderValue = musicSliderValue.value,
                    onSliderMove = viewModel::onMusicSliderMove,
                    onRepeatClick = viewModel::toggleRepeatStatus,
                    onPreviousClick = viewModel::fetchPreviousMusicFile,
                    onPlayClick = viewModel::togglePlayingStatus,
                    onNextClick = viewModel::fetchNextMusicFile,
                )
            }
        },
    )
}
