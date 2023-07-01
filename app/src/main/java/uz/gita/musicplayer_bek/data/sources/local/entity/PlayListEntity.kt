package uz.gita.musicplayer_bek.data.sources.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.gita.musicplayer_bek.data.model.PlayListData

@Entity(tableName = "playlists")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
) {
    fun toData() = PlayListData(id, name)
}