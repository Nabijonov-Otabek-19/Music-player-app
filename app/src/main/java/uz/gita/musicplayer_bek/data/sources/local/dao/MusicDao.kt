package uz.gita.musicplayer_bek.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.gita.musicplayer_bek.data.sources.local.entity.MusicEntity
import uz.gita.musicplayer_bek.data.sources.local.entity.PlayListEntity

@Dao
interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMusic(musicEntity: MusicEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPlayList(playListEntity: PlayListEntity)

    @Delete
    fun delete(musicEntity: MusicEntity)

    @Query("Select * from musics")
    fun retrieveAllMusics(): Flow<List<MusicEntity>>

    @Query("Select * from playlists")
    fun retrieveAllPlaylists(): Flow<List<PlayListEntity>>
}