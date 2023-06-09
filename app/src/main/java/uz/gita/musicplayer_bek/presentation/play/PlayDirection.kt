package uz.gita.musicplayer_bek.presentation.play

import uz.gita.musicplayer_bek.navigation.AppNavigator
import javax.inject.Inject

class PlayDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
) : PlayContract.PlayDirection{

    override suspend fun back() {
        navigator.pop()
    }
}