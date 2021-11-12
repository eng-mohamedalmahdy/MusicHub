package com.example.musichub

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.musichub.data.MusicPlayerControlCenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@HiltAndroidApp
class MusicHubApplication : Application() {

    companion object {
        const val CHANNEL_ID = "MUSIC_HUB"
        const val CHANNEL_NAME = "MusicHub player notification "

    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
    }

    private fun createNotificationChannel(manager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_MAX
            )
            manager.createNotificationChannel(notificationChannel)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context) =
        MusicPlayerControlCenter(appContext)

}
