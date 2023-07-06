package uz.gita.musicplayer_bek.presentation.play

import uz.gita.musicplayer_bek.navigation.AppNavigator
import javax.inject.Inject

class PlayDirection @Inject constructor(
    private val navigator: AppNavigator
) : PlayContract.Direction{

    override suspend fun back() {
        navigator.pop()
    }
}