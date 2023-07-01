package uz.gita.musicplayer_bek.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.gita.musicplayer_bek.data.sources.local.entity.MusicEntity

@Dao
interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(musicEntity: MusicEntity)

    @Delete
    fun delete(musicEntity: MusicEntity)

    @Query("Select * from musics")
    fun retrieveAllMusics(): Flow<List<MusicEntity>>
}