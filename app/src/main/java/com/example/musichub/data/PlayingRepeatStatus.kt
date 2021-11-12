package com.example.musichub.data

sealed class PlayingRepeatStatus {
    object REPEAT_ALL : PlayingRepeatStatus()
    object REPEAT_ONE : PlayingRepeatStatus()
    object SHUFFLE : PlayingRepeatStatus()

    fun getNext(): PlayingRepeatStatus =
        when (this) {
            is REPEAT_ALL -> REPEAT_ONE
            is REPEAT_ONE -> SHUFFLE
            is SHUFFLE -> REPEAT_ALL
        }
}
