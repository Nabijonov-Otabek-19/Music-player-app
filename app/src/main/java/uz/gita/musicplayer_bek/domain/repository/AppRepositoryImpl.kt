package uz.gita.musicplayer_bek.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.gita.musicplayer_bek.data.model.MusicData
import uz.gita.musicplayer_bek.data.model.PlayListData
import uz.gita.musicplayer_bek.data.sources.local.dao.MusicDao
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val dao: MusicDao
) : AppRepository {

    override fun addMusic(musicData: MusicData) {
        dao.addMusic(musicData.toEntity())
    }

    override fun addPlayList(playListData: PlayListData) {
        dao.addPlayList(playListData.toEntity())
    }

    override fun deleteMusic(musicData: MusicData) {
        dao.deleteMusic(musicData.toEntity())
    }

    override fun deletePlayList(playListData: PlayListData) {
        dao.deletePlayList(playListData.toEntity())
    }

    override fun getAllMusics(): Flow<List<MusicData>> =
        dao.retrieveAllMusics().map { list ->
            list.map { musicEntity ->
                musicEntity.toData()
            }
        }

    override fun getAllPlaylists(): Flow<List<PlayListData>> =
        dao.retrieveAllPlaylists().map { list ->
            list.map { playListEntity ->
                playListEntity.toData()
            }
        }

    override fun checkMusicIsSaved(musicData: MusicData): Boolean {
        val data = dao.checkMusicSaved(musicData.data ?: "")
        return data != null
    }
}