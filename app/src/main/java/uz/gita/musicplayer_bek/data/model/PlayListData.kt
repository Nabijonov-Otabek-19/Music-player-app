package uz.gita.musicplayer_bek.data.model

import uz.gita.musicplayer_bek.data.sources.local.entity.PlayListEntity

data class PlayListData(
    val id: Int,
    val name: String
) {
    fun toEntity() = PlayListEntity(id, name)
}
