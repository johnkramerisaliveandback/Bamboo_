package com.example.data

object SoundLibrary {
    val ALARM_SOUNDS = listOf(
        SoundOption("Default", null),
        SoundOption("Digital Watch", "https://actions.google.com/sounds/v1/alarms/digital_watch_alarm_long.ogg"),
        SoundOption("Classic Bell", "https://www.soundjay.com/buttons/beep-01a.mp3"),
        SoundOption("Morning Glow", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"),
        SoundOption("Beep Loop", "https://actions.google.com/sounds/v1/alarms/alarm_clock.ogg")
    )
}

data class SoundOption(
    val name: String,
    val url: String?
)
