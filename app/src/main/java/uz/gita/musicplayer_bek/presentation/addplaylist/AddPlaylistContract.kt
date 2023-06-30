package uz.gita.musicplayer_bek.presentation.addplaylist

import org.orbitmvi.orbit.ContainerHost

interface AddPlaylistContract {
    interface ViewModel : ContainerHost<UIState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    sealed interface UIState {
        object Loading : UIState
        object PreparedData : UIState
    }

    sealed interface SideEffect {
        object OpenPermissionDialog : SideEffect
    }

    sealed interface Intent {
        object OpenPlaylistScreen : Intent
    }

    interface PlaylistDirection {
        suspend fun navigateToPlaylistScreen()
    }
}