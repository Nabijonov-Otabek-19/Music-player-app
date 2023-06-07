package uz.bellissimo.musicplayer.utils

import android.database.Cursor
import kotlinx.coroutines.flow.MutableStateFlow

object MyEventBus {
    var selectMusicPos: Int = -1
    var cursor: Cursor? = null

    var currentTime: Int = 0
    var totalTime: Int = 0

    val currentTimeFlow = MutableStateFlow(0)
}