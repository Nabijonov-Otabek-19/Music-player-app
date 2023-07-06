package uz.gita.musicplayer_bek.presentation.addplaylist

import uz.gita.musicplayer_bek.navigation.AppNavigator
import uz.gita.musicplayer_bek.presentation.playlist.PlayListScreen
import javax.inject.Inject

class AddPlaylistDirection @Inject constructor(
    private val appNavigator: AppNavigator
) : AddPlaylistContract.Direction {

    override suspend fun navigateToPlaylistScreen() {
        appNavigator.navigateTo(PlayListScreen())
    }
}