package com.example.musichub.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musichub.R
import com.example.musichub.ui.theme.Beige

@Composable
fun NavigationDrawerContent(
    onDrawerHomeItemClickListener: () -> Unit,
    onDrawerDownloadItemClickListener: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(start = 12.dp),
    ) {
        AboutApp()
        Divider()
        DrawerItems(
            onDrawerHomeItemClickListener,
            onDrawerDownloadItemClickListener
        )
    }
}

@Composable
fun DrawerItems(
    onDrawerHomeItemClickListener: () -> Unit,
    onDrawerDownloadItemClickListener: () -> Unit
) {
    Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top) {
        HomePageButton(onDrawerHomeItemClickListener)
        DownloadFromYouTubeButton(onDrawerDownloadItemClickListener)
    }
}

@Composable
fun DrawerItem(imageId: Int, text: String, onClick: () -> Unit) {
    Row(
        Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
            .fillMaxWidth()
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = imageId),
            contentDescription = "Download",
            modifier = Modifier.size(32.dp),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(Beige)
        )
        Text(
            text = text,
            Modifier.padding(start = 8.dp),
            fontSize = 24.sp,
            color = Beige
        )
    }
}

@Composable
private fun HomePageButton(onClick: () -> Unit) =
    DrawerItem(
        imageId = R.drawable.ic_baseline_home_24,
        text = LocalContext.current.getString(R.string.home),
        onClick = onClick
    )

@Composable
private fun DownloadFromYouTubeButton(onClick: () -> Unit) = DrawerItem(
    imageId = R.drawable.ic_cloud_download,
    text = LocalContext.current.getString(R.string.download_from_youtube),
    onClick = onClick
)

@Composable
private fun AboutApp() {
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = LocalContext.current.getString(R.string.app_name),
            Modifier.padding(start = 8.dp),
            fontSize = 48.sp,
            color = Beige
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_music_note_24),
            contentDescription = "App icon",
            modifier = Modifier.size(50.dp),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(Beige)
        )
    }
}
