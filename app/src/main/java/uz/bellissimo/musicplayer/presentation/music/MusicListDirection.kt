package uz.bellissimo.musicplayer.presentation.music

import uz.bellissimo.musicplayer.presentation.play.PlayScreen
import uz.bellissimo.musicplayer.navigation.AppNavigator
import javax.inject.Inject

class MusicListDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
) : MusicListContract.MusicListDirection {

    override suspend fun navigateToPlayScreen() {
        navigator.navigateTo(PlayScreen())
    }
}