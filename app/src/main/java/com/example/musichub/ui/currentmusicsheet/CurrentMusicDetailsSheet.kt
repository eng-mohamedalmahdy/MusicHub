package com.example.musichub.ui.currentmusicsheet


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.musichub.data.MusicFile
import com.example.musichub.ui.musiccontrollers.MusicFullController
import com.example.musichub.ui.musiclist.MusicListViewModel
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

    ModalBottomSheetLayout(
        sheetState = isShowingDetails,
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

                MusicItemImage(musicFile.getImage())

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
        sheetShape = Shapes.large,

        ) {
        content()
    }
}
