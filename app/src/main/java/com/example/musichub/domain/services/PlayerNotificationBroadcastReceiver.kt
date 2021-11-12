package com.example.musichub.domain.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PlayerNotificationBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val KEY_ACTION = "ACTION"
        const val ACTION_PLAY = "PLAY"
        const val ACTION_NEXT = "NEXT"
        const val ACTION_PREV = "PREV"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val musicPlayerServiceIntent = Intent(context, MusicPlayerService::class.java)
            .apply {
                putExtra(KEY_ACTION, intent.action)
            }
        context.startService(musicPlayerServiceIntent)
    }
}