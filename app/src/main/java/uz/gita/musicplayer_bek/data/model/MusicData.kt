package uz.gita.musicplayer_bek.data.model

import uz.gita.musicplayer_bek.data.sources.local.entity.MusicEntity

data class MusicData(
    val id: Int,
    val artist: String?,
    val title: String?,
    val data: String?,
    val duration: Long,
    val storagePosition: Int = 0
) {
    fun toEntity() = MusicEntity(id, artist, title, data, duration, storagePosition)
}