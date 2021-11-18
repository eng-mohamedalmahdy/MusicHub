package com.example.musichub.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.musichub.R
import com.example.musichub.ui.musicpage.MusicListViewModel
import com.example.musichub.ui.theme.Beige
import com.example.musichub.ui.theme.OrangeCrayola

@ExperimentalComposeUiApi
@Composable
fun HomeAppBar(
    topBarTitle: String,
    viewModel: MusicListViewModel,
    onDrawerIconClickListener: () -> Unit,

    ) {
    val filterValue = viewModel.currentFilter.collectAsState()
    TopAppBar(backgroundColor = OrangeCrayola, modifier = Modifier.sizeIn(minHeight = 70.dp)) {
        Row(
            Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_menu_24),
                contentDescription = "Navigation drawer button",
                colorFilter = ColorFilter.tint(Beige),
                modifier = Modifier.clickable { onDrawerIconClickListener() }
            )

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
