package uz.gita.musicplayer_bek.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.gita.musicplayer_bek.data.model.MusicData
import uz.gita.musicplayer_bek.data.sources.local.dao.MusicDao
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val dao: MusicDao
) : AppRepository {

    override fun getAllMusics(): Flow<List<MusicData>> =
        dao.retrieveAllMusics().map { list ->
            list.map { musicEntity ->
                musicEntity.toData()
            }
        }
}