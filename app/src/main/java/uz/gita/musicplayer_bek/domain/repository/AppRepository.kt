package uz.gita.musicplayer_bek.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.musicplayer_bek.data.model.MusicData
import uz.gita.musicplayer_bek.data.model.PlayListData

interface AppRepository {

    fun addMusic(musicData: MusicData)
    fun addPlayList(playListData: PlayListData)

    fun getAllMusics(): Flow<List<MusicData>>
    fun getAllPlaylists(): Flow<List<PlayListData>>
}