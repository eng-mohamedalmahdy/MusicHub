package com.example.musichub.domain

import android.app.DownloadManager
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.widget.Toast
import android.provider.MediaStore

import android.content.ContentValues
import android.os.Environment


fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

fun Context.toast(message: Int, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

fun Context.downloadMediaFile(
    url: Uri,
    title: String,
    description: String,
    duration: Long,
    artist: String = "Unknown Artist",
    album: String = "Unknown Album"
) {
    val request = DownloadManager.Request(url)
        .setTitle(title)
        .setDescription(description)
        .setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            title
        )
        .setAllowedOverMetered(true)
    val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    val requestId = downloadManager.enqueue(request)

    val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (requestId == p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)) {

                val contentValues = ContentValues()
                contentValues.put(
                    MediaStore.Audio.AudioColumns.DATA,
                    "${Environment.DIRECTORY_DOWNLOADS}/$title"
                )
                contentValues.put(MediaStore.Audio.AudioColumns.TITLE, title)
                contentValues.put(MediaStore.Audio.AudioColumns.DISPLAY_NAME, title)
                contentValues.put(MediaStore.Audio.AudioColumns.DURATION, duration)
                contentValues.put(MediaStore.Audio.AudioColumns.ARTIST, artist)
                contentValues.put(MediaStore.Audio.AudioColumns.ALBUM, album)

                val uri = contentResolver.insert(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
            }
        }
    }
    registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
}