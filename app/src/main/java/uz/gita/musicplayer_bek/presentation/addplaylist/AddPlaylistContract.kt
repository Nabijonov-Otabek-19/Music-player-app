package uz.gita.musicplayer_bek.presentation.addplaylist

import org.orbitmvi.orbit.ContainerHost
import uz.gita.musicplayer_bek.data.model.PlayListData

interface AddPlaylistContract {
    interface ViewModel : ContainerHost<UIState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    sealed interface UIState {
        object Loading : UIState
        data class PreparedData(val playListData: List<PlayListData>) : UIState
    }

    sealed interface SideEffect {
        object ShowDialog : SideEffect
    }

    sealed interface Intent {
        object LoadPlayLists : Intent
        object OpenPlaylistScreen : Intent
        object OpenDialog : Intent
        data class DeletePlayList(val playListData: PlayListData) : Intent
        data class AddPlayList(val playListData: PlayListData) : Intent
    }

    interface Direction {
        suspend fun navigateToPlaylistScreen()
    }
}