package com.example.musichub.ui.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.ModalBottomSheetValue.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.musichub.domain.toast
import com.example.musichub.ui.currentmusicsheet.CurrentMusicDetailsSheet
import com.example.musichub.ui.musiccontrollers.SmallMusicController
import com.example.musichub.ui.musicpage.MusicListViewModel
import com.example.musichub.ui.musicpage.MusicPage
import com.example.musichub.ui.theme.USAFABlue
import com.example.musichub.ui.youtubedownloadpage.DownloadFromYoutubePage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val HOME_PAGE_INDEX = 0
const val DOWNLOAD_PAGE_INDEX = 1
private const val TAG = "HomePage"

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun Home(
    topBarTitle: String = "MusicHub",
    musicListViewModel: MusicListViewModel
) {
    var currentPageIndex by remember { mutableStateOf(0) }
    val musicList = musicListViewModel.displayedMusicFiles.collectAsState()
    val isPlaying = musicListViewModel.playingStatus.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val isShowingMusicDetails = rememberModalBottomSheetState(Hidden)
    val isShowingNavigationDrawer =
        rememberScaffoldState(drawerState = DrawerState(initialValue = DrawerValue.Closed))
    val currentPlayingIndex = musicListViewModel.currentPlayingFile.collectAsState()


    Scaffold(
        scaffoldState = isShowingNavigationDrawer,
        drawerContent = {
            NavigationDrawerContent(
                onDrawerHomeItemClickListener = {
                    currentPageIndex = 0
                    coroutineScope.launch {
                        isShowingNavigationDrawer.drawerState.close()
                    }
                },
                onDrawerDownloadItemClickListener = {
                    currentPageIndex = 1
                    coroutineScope.launch {
                        isShowingNavigationDrawer.drawerState.close()
                    }
                })
        },
        drawerBackgroundColor = USAFABlue
    ) {
        Column {
            HomeAppBar(topBarTitle, viewModel = musicListViewModel) {
                val drawerStatus = isShowingNavigationDrawer.drawerState
                coroutineScope.launch {
                    if (drawerStatus.currentValue == DrawerValue.Closed) {
                        drawerStatus.open()
                    } else {
                        drawerStatus.close()
                    }
                }
            }

            CurrentMusicDetailsSheet(
                musicFile = currentPlayingIndex.value ?: return@Column,
                isShowingDetails = isShowingMusicDetails,
                viewModel = musicListViewModel
            ) {

                Column {

                    when (currentPageIndex) {
                        HOME_PAGE_INDEX -> {
                            MusicPage(musicList = musicList.value) {
                                musicListViewModel.updateCurrentMusicFile(it)
                                musicListViewModel.playMusicFile()

                            }
                            SmallMusicController(
                                currentMusicFile = currentPlayingIndex.value
                                    ?: return@CurrentMusicDetailsSheet,
                                isPlaying = isPlaying.value,
                                onContainerClick = {
                                    toggleMusicDetailsBottomSheet(
                                        coroutineScope,
                                        isShowingMusicDetails
                                    )
                                },
                                onPlayClick = { musicListViewModel.togglePlayingStatus() },
                                onNextClick = { musicListViewModel.fetchNextMusicFile() })
                        }
                        DOWNLOAD_PAGE_INDEX -> {
                            DownloadFromYoutubePage()
                        }
                    }


                }
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