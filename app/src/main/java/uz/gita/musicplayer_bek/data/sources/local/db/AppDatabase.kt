package uz.gita.musicplayer_bek.data.sources.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.gita.musicplayer_bek.data.sources.local.dao.MusicDao
import uz.gita.musicplayer_bek.data.sources.local.entity.MusicEntity
import uz.gita.musicplayer_bek.data.sources.local.entity.PlayListEntity

@Database(entities = [MusicEntity::class, PlayListEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMusicDao(): MusicDao
}