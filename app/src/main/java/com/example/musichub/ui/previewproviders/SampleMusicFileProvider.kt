package com.example.musichub.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.musichub.data.MusicFile

class SampleMusicFileProvider : PreviewParameterProvider<MusicFile> {
    override val values = sequenceOf(MusicFile(1, "Path", "Title", "Artist", "Album", "50"))
    override val count: Int = values.count()
}