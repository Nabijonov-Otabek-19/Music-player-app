package uz.gita.musicplayer_bek.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.musicplayer_bek.data.model.MusicData

interface AppRepository {
    fun getAllMusics(): Flow<List<MusicData>>
}