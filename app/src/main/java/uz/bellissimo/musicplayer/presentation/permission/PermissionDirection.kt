package uz.bellissimo.musicplayer.presentation.permission

import uz.bellissimo.musicplayer.presentation.music.MusicListScreen
import uz.bellissimo.musicplayer.navigation.AppNavigator
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


