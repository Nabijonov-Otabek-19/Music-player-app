package uz.bellissimo.musicplayer.presentation.play

import org.orbitmvi.orbit.ContainerHost

sealed interface PlayContract {

    interface ViewModel : ContainerHost<UIState, Nothing> {
        fun onEventDispatcher(intent: Intent)
    }

    sealed interface UIState {
        object Next : UIState
        object Prev : UIState
        object Manage : UIState
    }

    sealed interface Intent {
        object Next : Intent
        object Prev : Intent
        object Manage : Intent
    }
}