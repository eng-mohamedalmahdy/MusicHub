package com.example.musichub.data

sealed class MusicSortMethod {
    object ID : MusicSortMethod()
    object NAME : MusicSortMethod()
    object DURATION : MusicSortMethod()
}