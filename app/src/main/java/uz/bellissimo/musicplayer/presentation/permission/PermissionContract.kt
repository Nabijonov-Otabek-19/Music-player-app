package uz.bellissimo.musicplayer.presentation.permission

import org.orbitmvi.orbit.ContainerHost

sealed class PermissionContract {
    interface ViewModel : ContainerHost<UIState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    interface UIState {
        object InitState : UIState
    }

    interface SideEffect {
        object OpenPermissionDialog: SideEffect
    }

    interface Intent {
        object RequestPermission: Intent
        object OpenMusicListScreen: Intent
    }
}



