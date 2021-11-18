package com.example.musichub.ui.youtubedownloadpage

import android.net.Uri
import android.util.SparseArray
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.core.util.valueIterator
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.musichub.R
import com.example.musichub.domain.downloadMediaFile
import com.example.musichub.ui.theme.Beige
import com.example.musichub.ui.theme.PeachCrayola
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalComposeUiApi
@Composable
fun DownloadFromYoutubePage() {
    Column(
        Modifier
            .fillMaxSize()
            .background(Beige)
    ) {
        val cxt = LocalContext.current
        var isLoading by remember { mutableStateOf(false) }
        var downloadOptionsUrls by remember { mutableStateOf(listOf<YtFile>()) }
        val context = LocalContext.current
        var imageUrl by remember { mutableStateOf<String?>(null) }
        var currentVideoMeta by remember { mutableStateOf<VideoMeta?>(null) }
        var linkInputTextValue by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val youTubeExtractor = object : YouTubeExtractor(context) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                isLoading = false
                imageUrl = videoMeta?.hqImageUrl
                currentVideoMeta = videoMeta
                downloadOptionsUrls = (ytFiles ?: SparseArray(0))
                    .valueIterator()
                    .asSequence()
                    .filterNot { it.format.ext.equals("webm", true) }
                    .distinctBy { listOf(it.format.height, it.format.fps) }
                    .sortedBy { it.format.height }
                    .toList()
            }

        }
        if (imageUrl != null) {
            GlideImage(imageModel = imageUrl, modifier = Modifier.fillMaxWidth(), loading = {
                Column(Modifier.fillMaxWidth()) { CircularProgressIndicator() }
            },
                failure = {
                    Text(text = LocalContext.current.getString(R.string.failed_to_load))
                })
        }


        TextField(
            placeholder = { Text(text = LocalContext.current.getString(R.string.drop_download_link_here)) },
            value = linkInputTextValue,
            onValueChange = { linkInputTextValue = it },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                placeholderColor = Color.Gray
            ),
            modifier = Modifier
                .padding(12.dp)
                .background(PeachCrayola)
                .fillMaxWidth(),
        )
        Button(
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            onClick = {
                isLoading = true
                youTubeExtractor.extract(linkInputTextValue)
                keyboardController?.hide()

            }
        ) {
            Text(
                text = LocalContext.current.getString(R.string.process_download_link),
                color = Beige
            )
        }

        LazyColumn {
            val items = downloadOptionsUrls

            items(items) {
                DownloadFromYoutubeListItem(ytFile = it) {
                    cxt.downloadMediaFile(
                        Uri.parse(it.url),
                        title = "${currentVideoMeta?.title ?: ""}.${it.format.ext}",
                        description = currentVideoMeta?.shortDescription ?: "",
                        duration = currentVideoMeta?.videoLength ?: 0L,
                        artist = currentVideoMeta?.author ?: "Unknown Artist",
                    )
                }
            }
        }
    }


}

@Composable
fun DownloadFromYoutubeListItem(ytFile: YtFile, onClick: () -> Unit) {
    val isAudio = ytFile.format.height == -1
    val quality = if (isAudio) "Audio" else ytFile.format.height.toString()
    val extension = ytFile.format.ext
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        onClick = onClick
    ) {
        Text(
            text = "%-20s%-20s".format("Quality: $quality", "Extension: $extension"),
            color = Beige
        )
    }
}
