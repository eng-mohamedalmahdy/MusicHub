package com.example.musichub.ui.musiclist


import androidx.compose.material.*
import androidx.compose.runtime.*
import com.example.musichub.data.MusicFile

@ExperimentalMaterialApi
@Composable
fun MusicPage(musicList: List<MusicFile>, onItemClickListener: (idx: MusicFile) -> Unit) {

    MusicList(musicList = musicList, onItemClick = onItemClickListener)

}



