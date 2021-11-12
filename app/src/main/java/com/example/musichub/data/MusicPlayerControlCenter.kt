package com.example.musichub.data

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class MusicPlayerControlCenter @Inject constructor(val context: Context) {


    val allMusicFiles = MusicProvider.getAllMusic(context)
    private val _displayedMusicFiles =
        MutableStateFlow(MusicProvider.getAllMusic(context))
    val displayedMusicFiles = _displayedMusicFiles.asStateFlow()

    var mediaPlayer: MediaPlayer? = null
    val mediaPlayerCompletionListener = MediaPlayer.OnCompletionListener {
        GlobalScope.launch {
            fetchNextMusicFile()
        }
    }

    private val _currentSortMethod = MutableStateFlow<MusicSortMethod>(MusicSortMethod.NAME)
    val currentSortMethod = _currentSortMethod.asStateFlow()

    private val _currentFilter = MutableStateFlow("")
    val currentFilter = _currentFilter.asStateFlow()
    suspend fun updateSearchText(text: String) {
        _currentFilter.emit(text)
    }


    private fun getMusicListSize(): Int = allMusicFiles.size

    private val _playingStatus = MutableStateFlow(false)
    val playingStatus = _playingStatus.asStateFlow()

    private val _currentMusicFileStamp = MutableStateFlow(0L)
    val currentMusicFileStamp = _currentMusicFileStamp.asStateFlow()

    private val _currentMusicFileFormattedStamp = MutableStateFlow("00:00:00")
    val currentMusicFormattedFileStamp = _currentMusicFileFormattedStamp.asStateFlow()

    private val _currentMusicFilePercent = MutableStateFlow(0f)
    val currentMusicFilePercent = _currentMusicFilePercent.asStateFlow()

    private val _repeatingStatus =
        MutableStateFlow<PlayingRepeatStatus>(PlayingRepeatStatus.REPEAT_ALL)
    val repeatingStatus = _repeatingStatus.asStateFlow()

    private val _currentPlayingIndex = MutableStateFlow(0)
    val currentPlayingIndex = _currentPlayingIndex.asStateFlow()

    private val _currentPlayingFile = MutableStateFlow(allMusicFiles.firstOrNull())
    val currentPlayingFile = _currentPlayingFile.asStateFlow()
    private val _sliderPercent = MutableStateFlow(0f)

    val sliderPercent = _sliderPercent.asStateFlow()


    suspend fun togglePlayingStatus() {
        _playingStatus.emit(!_playingStatus.value)
        mediaPlayer?.toggle()
    }

    suspend fun onMusicSliderMove(percent: Float) {
        _currentMusicFilePercent.emit(percent)
        _sliderPercent.emit(percent)

    }


    suspend fun toggleRepeatStatus() {
        _repeatingStatus.emit(_repeatingStatus.value.getNext())

    }

    suspend fun fetchNextMusicFile() {
        val newValue = when (repeatingStatus.value) {
            is PlayingRepeatStatus.REPEAT_ALL -> (_currentPlayingIndex.value + 1) % getMusicListSize()
            is PlayingRepeatStatus.REPEAT_ONE -> _currentPlayingIndex.value
            is PlayingRepeatStatus.SHUFFLE -> Random(7).nextInt(0, getMusicListSize())
        }
        _currentPlayingIndex.emit(newValue)
        _currentMusicFileStamp.emit(0L)
    }

    suspend fun fetchPreviousMusicFile() {
        val newValue = when (repeatingStatus.value) {
            is PlayingRepeatStatus.REPEAT_ALL -> if (_currentPlayingIndex.value - 1 < 0)
                getMusicListSize() - 1 else _currentPlayingIndex.value - 1
            is PlayingRepeatStatus.REPEAT_ONE -> _currentPlayingIndex.value
            is PlayingRepeatStatus.SHUFFLE -> Random(7).nextInt(
                from = 0,
                until = getMusicListSize()
            )
        }
        _currentPlayingIndex.emit(newValue)
        _currentMusicFileStamp.emit(0L)
    }

    suspend fun updateCurrentMusicFile(musicFile: MusicFile?) {
        _currentPlayingFile.emit(musicFile)
        _currentPlayingIndex.emit(allMusicFiles.indexOf(musicFile))
    }

    suspend fun playMusicFile() {
        _playingStatus.emit(true)
        mediaPlayer?.start()
    }

    suspend fun updateCurrentMusicFileFormattedStamp(formattedAsTime: String) {
        _currentMusicFileFormattedStamp.emit(formattedAsTime)
    }

    suspend fun updateCurrentMusicFilePercent(percent: Float) {
        _currentMusicFilePercent.emit(percent)
    }

    suspend fun updateCurrentMusicFileStamp(value: Long) {
        _currentMusicFileStamp.emit(value)
    }

    suspend fun updateSliderPercent(percent: Float) {
        _sliderPercent.emit(percent)
    }

    suspend fun updateDisplayedMusicFiles(filter: List<MusicFile>) {
        _displayedMusicFiles.emit(filter)
    }

    suspend fun updateCurrentSortMethod(value: MusicSortMethod) {
        _currentSortMethod.emit(value)
    }

    fun updateMediaPlayer(mediaPlayer: MediaPlayer?) {
        this.mediaPlayer = mediaPlayer
    }
}

private fun MediaPlayer.toggle() {
    if (this.isPlaying) pause()
    else start()
}