package com.example.musichub.data

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

object MusicProvider {
    fun getAllMusic(context: Context): List<MusicFile> {
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        val uriExternal: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val cursor = context.contentResolver.query(uriExternal, projection, selection, null, null)

        val songs = mutableListOf<MusicFile>()
        while (cursor?.moveToNext() == true) {
            val id = cursor.getLong(0)
            val title = cursor.getString(1)
            val artist = cursor.getString(2)
            val album = cursor.getString(3)
            val duration = cursor.getString(4)
            val path = cursor.getString(5)
            val song = MusicFile(id, path, title, artist, album, duration)
            songs += song
        }
        cursor?.close()
        return songs
    }
}
