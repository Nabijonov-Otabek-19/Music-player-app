package uz.gita.musicplayer_bek.utils

import android.database.Cursor
import kotlinx.coroutines.flow.MutableStateFlow
import uz.gita.musicplayer_bek.data.model.CursorEnum
import uz.gita.musicplayer_bek.data.model.MusicData

object MyEventBus {
    var storageCursor: Cursor? = null
    var roomCursor: Cursor? = null

    var currentCursorEnum: CursorEnum? = null

    var storagePos: Int = -1
    var roomPos: Int = -1

    var totalTime: Int = 0
    var currentTime = MutableStateFlow(0)
    val currentTimeFlow = MutableStateFlow(0)

    var isPlaying = MutableStateFlow(false)
    val currentMusicData = MutableStateFlow<MusicData?>(null)
}