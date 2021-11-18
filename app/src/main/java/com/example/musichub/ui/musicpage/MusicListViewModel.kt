package com.example.musichub.ui.musicpage

import android.app.Application
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.musichub.data.*
import com.example.musichub.domain.formattedAsTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "MusicListViewModel"

@HiltViewModel
class MusicListViewModel @Inject constructor(
    application: Application,
    val repository: MusicPlayerControlCenter
) : AndroidViewModel(application) {

    private val allMusicFiles = repository.allMusicFiles
    private val mediaPlayerCompletionListener = repository.mediaPlayerCompletionListener
    val displayedMusicFiles = repository.displayedMusicFiles
    val currentFilter = repository.currentFilter
    fun updateSearchText(text: String) {
        viewModelScope.launch {
            repository.updateSearchText(text)
        }
    }

    val playingStatus = repository.playingStatus

    val currentMusicFormattedFileStamp = repository.currentMusicFormattedFileStamp

    val repeatingStatus = repository.repeatingStatus

    val sliderPercent = repository.sliderPercent

    val currentPlayingFile = repository.currentPlayingFile

    private val currentSortMethod = repository.currentSortMethod

    private val _currentMusicFileStamp = repository.currentMusicFileStamp

    private val _currentMusicFilePercent = repository.currentMusicFilePercent

    private val _currentPlayingIndex = repository.currentPlayingIndex

    init {
        viewModelScope.launch {
            currentPlayingFile.filterNotNull().collectLatest {
                repository.mediaPlayer?.stop()
                repository.mediaPlayer?.release()
                repository.updateMediaPlayer(
                    MediaPlayer.create(
                        getApplication(),
                        Uri.parse(it.path)
                    )
                )
                repository.mediaPlayer?.setOnCompletionListener(mediaPlayerCompletionListener)
                if (playingStatus.value) {
                    repository.mediaPlayer?.start()
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            _currentMusicFileStamp.collect {
                val currentFileDuration = (currentPlayingFile.value?.duration ?: "0").toLong()
                val percent = it.toFloat() / currentFileDuration
                repository.updateCurrentMusicFileFormattedStamp(it.formattedAsTime())
                repository.updateCurrentMusicFilePercent(percent)
                CoroutineScope(Dispatchers.Main).launch {
                    repository.mediaPlayer?.seekTo(it.toInt())
                }
            }
        }
        viewModelScope.launch {
            _currentMusicFilePercent.collect {
                val currentFileDuration = (currentPlayingFile.value?.duration ?: "0").toLong()
                repository.updateCurrentMusicFileStamp((currentFileDuration * it).toLong())
                repository.updateCurrentMusicFileFormattedStamp(_currentMusicFileStamp.value.formattedAsTime())
            }
        }
        viewModelScope.launch {
            _currentPlayingIndex.collect {
                repository.updateCurrentMusicFile(allMusicFiles.getOrNull(it))
            }
        }
        viewModelScope.launch {
            currentSortMethod.collect {
                when (it) {
                    is MusicSortMethod.ID -> {
                        repository.updateDisplayedMusicFiles(displayedMusicFiles.value.sortedBy { musicFile ->
                            musicFile.id.toString()
                        })
                    }
                    is MusicSortMethod.NAME ->
                        repository.updateDisplayedMusicFiles(displayedMusicFiles.value.sortedBy { musicFile ->
                            musicFile.title
                        })

                    MusicSortMethod.DURATION -> repository.updateDisplayedMusicFiles(
                        displayedMusicFiles.value.sortedBy { musicFile ->
                            musicFile.duration
                        })
                }
            }
        }
        viewModelScope.launch {
            currentFilter.collect { filter ->
                repository.updateDisplayedMusicFiles(allMusicFiles.filter { musicFile ->
                    musicFile.contains(filter)
                })
                repository.updateCurrentSortMethod(currentSortMethod.value)
            }
        }

        CoroutineScope(Dispatchers.Default).launch {

            while (repository.mediaPlayer != null) {
                try {
                    val currentPosition = repository.mediaPlayer?.currentPosition?.toLong() ?: 0L
                    val totalDuration = (currentPlayingFile.value?.duration ?: "0").toLong()
                    repository.updateCurrentMusicFileFormattedStamp(currentPosition.formattedAsTime())
                    repository.updateSliderPercent(currentPosition.toFloat() / totalDuration)
                    delay(1000)
                } catch (ex: Exception) {
                    Log.d(TAG, ex.localizedMessage ?: ex.toString())
                }
            }
        }

    }

    fun onMusicSliderMove(value: Float) {
        viewModelScope.launch {
            repository.onMusicSliderMove(value)
        }
    }

    fun toggleRepeatStatus() {
        viewModelScope.launch {
            repository.toggleRepeatStatus()
        }
    }

    fun fetchPreviousMusicFile() {
        viewModelScope.launch {
            repository.fetchPreviousMusicFile()
        }
    }

    fun togglePlayingStatus() {
        viewModelScope.launch {
            repository.togglePlayingStatus()
        }
    }

    fun fetchNextMusicFile() {
        viewModelScope.launch {
            repository.fetchNextMusicFile()
        }
    }

    fun playMusicFile() {
        viewModelScope.launch {
            repository.playMusicFile()
        }
    }

    fun updateCurrentMusicFile(musicFile: MusicFile) {
        viewModelScope.launch {
            repository.updateCurrentMusicFile(musicFile)
        }
    }

}
