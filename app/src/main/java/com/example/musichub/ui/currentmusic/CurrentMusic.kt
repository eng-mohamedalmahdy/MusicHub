package com.example.musichub.ui.currentmusic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musichub.data.MusicFile


//
//val bottomSheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
//    bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
//)
@ExperimentalMaterialApi
@Composable
fun CurrentPlayingMusic(musicFile: MusicFile,bottomSheetScaffoldState: BottomSheetScaffoldState) {

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Text(text = "Hello from sheet")
            }
        }, sheetPeekHeight = 0.dp
    ){

    }
}