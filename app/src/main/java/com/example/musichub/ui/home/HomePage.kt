package com.example.musichub.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.example.musichub.ui.currentmusicsheet.CurrentMusicDetailsSheet
import com.example.musichub.ui.musiclist.MusicListViewModel
import com.example.musichub.ui.musiccontrollers.SmallMusicController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material.ModalBottomSheetValue.*
import com.example.musichub.ui.musiclist.MusicPage

@ExperimentalMaterialApi
@Composable
fun Home(
    topBarTitle: String = "MusicHub",
    musicListViewModel: MusicListViewModel
) {
    val musicList = musicListViewModel.displayedMusicFiles.collectAsState()
    val isPlaying = musicListViewModel.playingStatus.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val isShowingMusicDetails = rememberModalBottomSheetState(Hidden)
    val currentPlayingIndex = musicListViewModel.currentPlayingFile.collectAsState()
    Column {

        HomeAppBar(topBarTitle, viewModel = musicListViewModel)

        CurrentMusicDetailsSheet(
            musicFile = currentPlayingIndex.value ?: return,
            isShowingDetails = isShowingMusicDetails,
            viewModel = musicListViewModel
        ) {

            Column {
                MusicPage(musicList = musicList.value) {
                    musicListViewModel.updateCurrentMusicFile(it)
                    musicListViewModel.playMusicFile()
                }

                SmallMusicController(
                    currentMusicFile = currentPlayingIndex.value ?: return@CurrentMusicDetailsSheet,
                    isPlaying = isPlaying.value,
                    onContainerClick = {
                        toggleMusicDetailsBottomSheet(coroutineScope, isShowingMusicDetails)
                    },
                    onPlayClick = { musicListViewModel.togglePlayingStatus() },
                    onNextClick = { musicListViewModel.fetchNextMusicFile() })
            }
        }

    }
}

@ExperimentalMaterialApi
fun toggleMusicDetailsBottomSheet(
    coroutineScope: CoroutineScope,
    isShowingMusicDetails: ModalBottomSheetState
) {
    coroutineScope.launch {
        if (isShowingMusicDetails.isVisible) {
            isShowingMusicDetails.animateTo(Hidden)
        } else {
            isShowingMusicDetails.animateTo(Expanded)
        }
    }
}