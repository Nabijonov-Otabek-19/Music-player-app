package uz.gita.musicplayer_bek.presentation.play

import org.orbitmvi.orbit.ContainerHost
import uz.gita.musicplayer_bek.data.model.ActionEnum

sealed interface PlayContract {

    interface ViewModel : ContainerHost<UIState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    sealed interface UIState {
        object InitState : UIState
    }

    sealed interface SideEffect {
        data class UserAction(val actionEnum: ActionEnum): SideEffect
    }

    sealed interface Intent {
        data class UserAction(val actionEnum: ActionEnum): Intent
    }

    interface PlayDirection {
        suspend fun back()
    }
}