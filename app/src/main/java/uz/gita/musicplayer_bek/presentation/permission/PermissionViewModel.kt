package uz.gita.musicplayer_bek.presentation.permission

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val direction: PermissionDirection
) : ViewModel(), PermissionContract.ViewModel {

    override val container =
        container<PermissionContract.UIState, PermissionContract.SideEffect>(PermissionContract.UIState.InitState)


    override fun onEventDispatcher(intent: PermissionContract.Intent) {
        when (intent) {
            PermissionContract.Intent.RequestPermission -> {
                intent { postSideEffect(PermissionContract.SideEffect.OpenPermissionDialog) }
            }

            PermissionContract.Intent.OpenMusicListScreen -> {
                intent { direction.openMusicListScreen() }
            }
        }
    }
}