package uz.gita.musicplayer_bek.utils.base

import uz.gita.musicplayer_bek.presentation.playlist.PlayListContract
import uz.gita.musicplayer_bek.utils.MyEventBus
import uz.gita.musicplayer_bek.utils.logger

suspend fun checkMusics(onEventDispatcher: (PlayListContract.Intent) -> Unit) {
    for (i in 0 until MyEventBus.roomCursor!!.count) {
        val roomData = MyEventBus.roomCursor!!.getMusicDataByPosition(i)
        var bool = true
        for (j in 0 until MyEventBus.storageCursor!!.count) {
            val storageData = MyEventBus.storageCursor!!.getMusicDataByPosition(j)
            bool = bool && roomData != storageData
        }
        if (bool) {
            onEventDispatcher.invoke(PlayListContract.Intent.DeleteMusic(roomData))
            logger("Music deleted = ${roomData.title}")
        }
    }
}