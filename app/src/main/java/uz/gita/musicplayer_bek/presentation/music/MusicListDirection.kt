package uz.gita.musicplayer_bek.presentation.music

import uz.gita.musicplayer_bek.presentation.play.PlayScreen
import uz.gita.musicplayer_bek.navigation.AppNavigator
import uz.gita.musicplayer_bek.presentation.playlist.PlayListScreen
import javax.inject.Inject

class MusicListDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
) : MusicListContract.MusicListDirection {

    override suspend fun navigateToPlayScreen() {
        navigator.navigateTo(PlayScreen())
    }

    override suspend fun navigateToPlayListScreen() {
        navigator.navigateTo(PlayListScreen())
    }
}