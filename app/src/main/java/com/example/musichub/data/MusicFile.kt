package com.example.musichub.data

import android.media.MediaMetadataRetriever
import com.example.musichub.domain.formattedAsTime

data class MusicFile(
    val id: Long,
    val path: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: String,
) {
    fun getImage(): ByteArray {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val art = retriever.embeddedPicture
        retriever.release()
        return art ?: byteArrayOf()
    }

    fun getFormattedDuration(): String = duration.formattedAsTime()
    fun getProperName(): String = title.take(10) + "..."

    fun contains(key: String) =
        title.contains(key, true) ||
                path.contains(key, true) ||
                artist.contains(key, true) ||
                album.contains(key, true) ||
                duration.contains(key, true) ||
                key.isEmpty()


}