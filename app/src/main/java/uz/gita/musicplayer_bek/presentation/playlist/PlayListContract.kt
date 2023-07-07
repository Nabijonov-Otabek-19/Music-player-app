package uz.gita.musicplayer_bek.presentation.playlist

import android.content.Context
import org.orbitmvi.orbit.ContainerHost
import uz.gita.musicplayer_bek.data.model.MusicData

interface PlayListContract {

    interface ViewModel : ContainerHost<UIState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    sealed interface UIState {
        object Loading : UIState
        object PreparedData : UIState
        //data class PreparedData(val savedMusics: List<MusicData>) : UIState
    }

    sealed interface SideEffect {
        object OpenPermissionDialog : SideEffect
        object StartMusicService : SideEffect
    }

    sealed interface Intent {
        object LoadMusics : Intent
        object PlayMusic : Intent
        object OpenPlayScreen : Intent
        object RequestPermission : Intent
        data class DeleteMusic(val musicData: MusicData) : Intent
    }

    interface Direction {
        suspend fun navigateToPlayScreen()
    }
}