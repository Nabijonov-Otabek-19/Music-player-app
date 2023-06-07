package uz.gita.musicplayer_bek.utils

import android.database.Cursor
import kotlinx.coroutines.flow.MutableStateFlow
import uz.gita.musicplayer_bek.data.model.MusicData

object MyEventBus {
    var selectMusicPos: Int = -1
    var cursor: Cursor? = null

    var currentTime: Int = 0
    var totalTime: Int = 0

    var isPlaying = MutableStateFlow(false)

    val currentMusicData = MutableStateFlow<MusicData?>(null)
    val currentTimeFlow = MutableStateFlow(0)
}