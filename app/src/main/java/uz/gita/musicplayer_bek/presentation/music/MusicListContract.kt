package uz.gita.musicplayer_bek.presentation.music

import android.content.Context
import org.orbitmvi.orbit.ContainerHost

sealed interface MusicListContract {

    interface ViewModel : ContainerHost<UIState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    sealed interface UIState {
        object Loading : UIState
        object PreparedData : UIState
    }

    sealed interface SideEffect {
        object StartMusicService : SideEffect
    }

    sealed interface Intent {
        data class LoadMusics(val context: Context) : Intent
        object PlayMusic : Intent
        object OpenPlayScreen : Intent
    }

    interface MusicListDirection {
        suspend fun navigateToPlayScreen()
    }
}