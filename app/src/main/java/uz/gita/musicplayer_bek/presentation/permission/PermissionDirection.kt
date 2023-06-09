package uz.gita.musicplayer_bek.presentation.permission

import uz.gita.musicplayer_bek.presentation.music.MusicListScreen
import uz.gita.musicplayer_bek.navigation.AppNavigator
import javax.inject.Inject

interface PermissionDirection {
    suspend fun openMusicListScreen()
}

class PermissionDirectionImpl @Inject constructor(
    private val navigator : AppNavigator
) : PermissionDirection {

    override suspend fun openMusicListScreen() {
        navigator.replace(MusicListScreen())
    }
}