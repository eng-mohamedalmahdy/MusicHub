package com.example.musichub.domain.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
import com.example.musichub.MainActivity
import com.example.musichub.MusicHubApplication
import com.example.musichub.R
import com.example.musichub.data.MusicFile
import com.example.musichub.data.MusicPlayerControlCenter
import com.example.musichub.domain.services.PlayerNotificationBroadcastReceiver.Companion.ACTION_NEXT
import com.example.musichub.domain.services.PlayerNotificationBroadcastReceiver.Companion.ACTION_PLAY
import com.example.musichub.domain.services.PlayerNotificationBroadcastReceiver.Companion.ACTION_PREV
import com.example.musichub.domain.services.PlayerNotificationBroadcastReceiver.Companion.KEY_ACTION
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MusicPlayerService : Service() {
    private val binder = MusicPlayerServiceBinder()

    @Inject
    lateinit var repository: MusicPlayerControlCenter

    private lateinit var mediaSession: MediaSessionCompat


    override fun onBind(intent: Intent) = binder

    inner class MusicPlayerServiceBinder : Binder() {
        fun getService() = this@MusicPlayerService
    }

    override fun onCreate() {
        super.onCreate()
        val currentMusicFile = repository.currentPlayingFile.value
        mediaSession = MediaSessionCompat(this, getString(R.string.app_name))
        startForeground(7, createNotification(mediaSession, currentMusicFile!!))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val actionName = intent?.getStringExtra(KEY_ACTION)

        CoroutineScope(Dispatchers.Default).launch {
            when (actionName) {
                ACTION_PLAY -> repository.togglePlayingStatus()
                ACTION_NEXT -> repository.fetchNextMusicFile()
                ACTION_PREV -> repository.fetchPreviousMusicFile()
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            repository.playingStatus.collect {
                val currentMusicFile = repository.currentPlayingFile.value
                startForeground(7, createNotification(mediaSession, currentMusicFile!!))
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            repository.currentPlayingFile.collect {
                startForeground(7, createNotification(mediaSession, it!!))
            }
        }
        return START_STICKY
    }

    private fun createNotification(
        mediaSession: MediaSessionCompat,
        currentMusicFile: MusicFile
    ): Notification {

        val intent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val playIntent = Intent(this, PlayerNotificationBroadcastReceiver::class.java).apply {
            action = ACTION_PLAY
        }
        val playPendingIntent =
            PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val playIcon =
            if (!repository.playingStatus.value) R.drawable.ic_play else R.drawable.ic_pause
        val playAction =
            NotificationCompat.Action.Builder(playIcon, "Play", playPendingIntent).build()


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


        val image = BitmapFactory.decodeByteArray(
            currentMusicFile.getImage(),
            0,
            currentMusicFile.getImage().size
        )
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, MusicHubApplication.CHANNEL_ID)

        } else {
            NotificationCompat.Builder(this)
        }.apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setLargeIcon(image)
            setContentTitle(currentMusicFile.title)
            setContentText(currentMusicFile.artist)
            addAction(prevAction)
            addAction(playAction)
            addAction(nextAction)
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0)
            )
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(contentIntent)
            setAutoCancel(false)
            setOngoing(true)
            foregroundServiceBehavior = FOREGROUND_SERVICE_IMMEDIATE
        }.build().apply {
            this.flags =
                NotificationCompat.FLAG_NO_CLEAR or
                        NotificationCompat.FLAG_ONGOING_EVENT or
                        NotificationCompat.FLAG_FOREGROUND_SERVICE
        }
        return notification
    }
}