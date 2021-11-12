package com.example.musichub.domain


import java.util.concurrent.TimeUnit

fun String.formattedAsTime(): String = String.format(
    "%02d:%02d:%02d",
    TimeUnit.MILLISECONDS.toHours(this.toLong()),
    TimeUnit.MILLISECONDS.toMinutes(this.toLong()) % TimeUnit.HOURS.toMinutes(1),
    TimeUnit.MILLISECONDS.toSeconds(this.toLong()) % TimeUnit.MINUTES.toSeconds(1)
)
fun Long.formattedAsTime(): String = String.format(
    "%02d:%02d:%02d",
    TimeUnit.MILLISECONDS.toHours(this),
    TimeUnit.MILLISECONDS.toMinutes(this) % TimeUnit.HOURS.toMinutes(1),
    TimeUnit.MILLISECONDS.toSeconds(this) % TimeUnit.MINUTES.toSeconds(1)
)




