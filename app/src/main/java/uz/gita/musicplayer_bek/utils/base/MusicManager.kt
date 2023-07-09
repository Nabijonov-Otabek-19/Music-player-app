package uz.gita.musicplayer_bek.utils.base

import uz.gita.musicplayer_bek.data.model.MusicData
import uz.gita.musicplayer_bek.utils.MyEventBus
import uz.gita.musicplayer_bek.utils.logger

fun checkMusicExistance(musicData: MusicData): Boolean {

    // Check if musicData exists in the storage list
    for (pos in 0 until MyEventBus.storageCursorForChecking!!.count) {
        logger("Storage loop = $pos")
        val data = MyEventBus.storageCursorForChecking!!.getMusicDataByPosition(pos)
        if (musicData == data) {
            return true
        }
    }
    // MusicData not found in storage
    return false
}