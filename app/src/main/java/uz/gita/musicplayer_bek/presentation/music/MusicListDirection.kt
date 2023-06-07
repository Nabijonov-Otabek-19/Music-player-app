package uz.gita.musicplayer_bek.presentation.music

import uz.gita.musicplayer_bek.presentation.play.PlayScreen
import uz.gita.musicplayer_bek.navigation.AppNavigator
import javax.inject.Inject

class MusicListDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
) : MusicListContract.MusicListDirection {

    override suspend fun navigateToPlayScreen() {
        navigator.navigateTo(PlayScreen())
    }
}