package com.example.musichub.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musichub.ui.musiclist.MusicList
import com.example.musichub.ui.musiclist.MusicListViewModel
import com.example.musichub.ui.theme.OrangeCrayola
import com.example.musichub.ui.theme.PeachCrayola

@Composable
fun Home(
    topBarTitle: String = "MusicGub",
    firstTabText: String = "Music",
    secondTapText: String = "Albums",
    musicListViewModel: MusicListViewModel
) {

    Column {
        var tabIndex by remember { mutableStateOf(0) }
        HomeAppBar(topBarTitle)
        HomeTapBar(
            tabIndex,
            firstTabText,
            secondTapText,
            firstTabClickListener = { tabIndex = 0 },
            secondTapClickListener = { tabIndex = 1 })
        PagesContainer(tabIndex, musicListViewModel)


    }
}


@Composable
fun HomeAppBar(topBarTitle: String) {
    TopAppBar(backgroundColor = OrangeCrayola) {
        Text(color = Color.White, text = topBarTitle)
    }
}

@Composable
fun HomeTapBar(
    tabIndex: Int,
    firstTabText: String,
    secondTapText: String,
    firstTabClickListener: () -> Unit,
    secondTapClickListener: () -> Unit
) {
    val tapPadding = 20.dp

    TabRow(backgroundColor = PeachCrayola, selectedTabIndex = tabIndex) {
        Tab(
            selected = true,
            onClick = { firstTabClickListener() }) {
            Text(text = firstTabText, modifier = Modifier.padding(tapPadding))
        }
        Tab(
            selected = false,
            onClick = { secondTapClickListener() }) {
            Text(text = secondTapText, modifier = Modifier.padding(tapPadding))
        }
    }
}

@Composable
fun PagesContainer(tabIndex: Int, musicListViewModel: MusicListViewModel) {
    if (tabIndex == 0) {
        MusicList(musicListViewModel)
    }
}

