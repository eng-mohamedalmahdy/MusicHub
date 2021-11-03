package com.example.musichub.data

import android.media.MediaMetadataRetriever
import java.text.SimpleDateFormat
import java.util.*

data class MusicFile(
    val id: Long,
    val path: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: String,
) {
    fun getImage(): ByteArray? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val art = retriever.embeddedPicture
        retriever.release()
        return art
    }

    fun getFormattedDuration(): String = SimpleDateFormat(
        "HH:mm:ss",
        Locale.getDefault()
    ).format(duration.toLong())


}