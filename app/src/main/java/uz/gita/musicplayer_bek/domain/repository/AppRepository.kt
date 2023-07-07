package uz.gita.musicplayer_bek.domain.repository

import android.database.Cursor
import kotlinx.coroutines.flow.Flow
import uz.gita.musicplayer_bek.data.model.MusicData

interface AppRepository {

    fun addMusic(musicData: MusicData)
    fun deleteMusic(musicData: MusicData)
    fun getAllMusics(): Flow<List<MusicData>>

    fun getSavedMusics(): Cursor

    fun checkMusicIsSaved(musicData: MusicData): Boolean
}