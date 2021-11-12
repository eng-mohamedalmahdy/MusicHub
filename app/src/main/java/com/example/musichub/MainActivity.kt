package com.example.musichub

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.musichub.domain.services.MusicPlayerService
import com.example.musichub.domain.services.PlayerNotificationBroadcastReceiver
import com.example.musichub.domain.services.PlayerNotificationBroadcastReceiver.Companion.ACTION_NEXT
import com.example.musichub.domain.services.PlayerNotificationBroadcastReceiver.Companion.ACTION_PLAY
import com.example.musichub.domain.services.PlayerNotificationBroadcastReceiver.Companion.ACTION_PREV
import com.example.musichub.ui.home.Home
import com.example.musichub.ui.musiclist.MusicListViewModel
import com.example.musichub.ui.theme.MusicHubTheme
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"


@AndroidEntryPoint
class MainActivity : ComponentActivity(), ServiceConnection {
    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>
    private val musicListViewModel: MusicListViewModel by viewModels()
    private var musicPlayerService: MusicPlayerService? = null
    private lateinit var mediaSession: MediaSessionCompat

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicHubTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Home(
                        topBarTitle = getString(R.string.app_name),
                        musicListViewModel = musicListViewModel
                    )
                }
            }
        }
        requestPermissions()

        mediaSession = MediaSessionCompat(this, getString(R.string.app_name))
        initMusicService()
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as MusicPlayerService.MusicPlayerServiceBinder
        musicPlayerService = binder.getService()
        Log.d(TAG, "onServiceDisconnected: Music player service connected")

    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicPlayerService = null
        Log.d(TAG, "onServiceDisconnected: Music player service disconnected")
    }

    override fun onPause() {
        super.onPause()
        unbindService(this)
    }

    override fun onResume() {
        super.onResume()
        initMusicService()
    }

    private fun initMusicService() {
        val intent = Intent(this, MusicPlayerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, intent)
        } else {
            startService(intent)
        }
        bindService(intent, this, BIND_AUTO_CREATE)
    }

    private fun requestPermissions() {
        requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.MANAGE_EXTERNAL_STORAGE] == true ||
                    (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
                            permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true)
                ) {

                } else {
                    launchPermissionsRequest(requestMultiplePermissions)
                }
            }

        launchPermissionsRequest(requestMultiplePermissions)
    }

    private fun launchPermissionsRequest(requestMultiplePermissions: ActivityResultLauncher<Array<String>>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

                    )
            )
        } else {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                )
            )
        }
    }

    private fun showPlayerNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val playIntent = Intent(this, PlayerNotificationBroadcastReceiver::class.java).apply {
            action = ACTION_PLAY
        }
        val playPendingIntent =
            PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val playAction =
            NotificationCompat.Action.Builder(R.drawable.ic_play, "Play", playPendingIntent).build()


        val playNextIntent = Intent(this, PlayerNotificationBroadcastReceiver::class.java).apply {
            action = ACTION_NEXT
        }
        val playNextPendingIntent =
            PendingIntent.getBroadcast(this, 0, playNextIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val nextAction =
            NotificationCompat.Action.Builder(
                R.drawable.ic_skip_next,
                "Next",
                playNextPendingIntent
            ).build()


        val playPrevIntent = Intent(this, PlayerNotificationBroadcastReceiver::class.java).apply {
            action = ACTION_PREV
        }
        val playPrevPendingIntent =
            PendingIntent.getBroadcast(this, 0, playPrevIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val prevAction =
            NotificationCompat.Action.Builder(
                R.drawable.ic_previous,
                "Previous",
                playPrevPendingIntent
            )
                .build()


        val currentMusicFile = musicListViewModel.currentPlayingFile.value
        val image = BitmapFactory.decodeByteArray(
            currentMusicFile?.getImage(),
            0,
            currentMusicFile?.getImage()?.size ?: 0
        )
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, MusicHubApplication.CHANNEL_ID)

        } else {
            NotificationCompat.Builder(this)
        }.apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setLargeIcon(image)
            setContentTitle(currentMusicFile?.title)
            setContentText(currentMusicFile?.artist)
            addAction(prevAction)
            addAction(playAction)
            addAction(nextAction)
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
            )
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(contentIntent)
            setAutoCancel(false)
            setOngoing(true)
        }.build().apply {
            flags = Notification.FLAG_NO_CLEAR or Notification.FLAG_ONGOING_EVENT
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, notification)
    }

}
