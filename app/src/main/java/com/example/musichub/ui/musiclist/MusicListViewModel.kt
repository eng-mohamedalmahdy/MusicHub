package com.example.musichub.ui.musiclist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.musichub.data.MusicProvider
import javax.inject.Inject

class MusicListViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    fun getAllMusic() = MusicProvider.getAllMusic(getApplication())
}