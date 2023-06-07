package uz.bellissimo.musicplayer.presentation.play

import uz.bellissimo.musicplayer.navigation.AppNavigator
import javax.inject.Inject

interface PlayDirection {
}

class PlayDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
) : PlayDirection{

}