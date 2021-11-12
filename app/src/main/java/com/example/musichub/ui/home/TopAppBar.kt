package com.example.musichub.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.musichub.ui.musiclist.MusicListViewModel
import com.example.musichub.ui.theme.Beige
import com.example.musichub.ui.theme.OrangeCrayola

@Composable
fun HomeAppBar(topBarTitle: String, viewModel: MusicListViewModel) {
    val filterValue = viewModel.currentFilter.collectAsState()
    TopAppBar(backgroundColor = OrangeCrayola, modifier = Modifier.sizeIn(minHeight = 70.dp)) {
        Row(
            Modifier
                .padding(8.dp)
                .background(OrangeCrayola)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(color = Color.White, text = topBarTitle)
            TextField(
                placeholder = { Text(text = "Search") },
                value = filterValue.value,
                onValueChange = viewModel::updateSearchText,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    placeholderColor = Color.Gray
                ),
                trailingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.Black
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Beige),
            )
        }
    }
}
