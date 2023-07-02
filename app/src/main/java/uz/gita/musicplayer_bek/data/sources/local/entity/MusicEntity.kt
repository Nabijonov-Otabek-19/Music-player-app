package uz.gita.musicplayer_bek.data.sources.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import uz.gita.musicplayer_bek.data.model.MusicData

@Entity(tableName = "musics",
    /*foreignKeys = [ForeignKey(entity = PlayListEntity::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("playlistId"),
    onDelete = ForeignKey.CASCADE)]*/
)
data class MusicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val artist: String?,
    val title: String?,
    val data: String?,
    val duration: Long,
    val playlistId: Int = 1
) {
    fun toData() = MusicData(id, artist, title, data, duration)
}