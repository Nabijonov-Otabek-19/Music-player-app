package uz.gita.musicplayer_bek.presentation.play

import uz.gita.musicplayer_bek.navigation.AppNavigator
import javax.inject.Inject

interface PlayDirection {
}

class PlayDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
) : PlayDirection{

}