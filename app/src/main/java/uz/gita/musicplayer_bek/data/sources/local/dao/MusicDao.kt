package uz.gita.musicplayer_bek.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.gita.musicplayer_bek.data.model.MusicData

@Dao
interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(musicData: MusicData)

    @Delete
    fun delete(musicData: MusicData)

    @Query("Select * from musics")
    fun retrieveAllContacts(): Flow<List<MusicData>>
}