package uz.bellissimo.musicplayer.presentation.permission

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import uz.bellissimo.musicplayer.utils.MyEventBus
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val direction: PermissionDirection
) : ViewModel(), PermissionContract.ViewModel {
    override val container= container<PermissionContract.UIState, PermissionContract.SideEffect>(PermissionContract.UIState.InitState)

    init {
        MyEventBus.currentTimeFlow.onEach {
            Log.d("TTT", "time = $it")
        }.launchIn(viewModelScope)
    }

    override fun onEventDispatcher(intent: PermissionContract.Intent) {
        when(intent) {
            PermissionContract.Intent.RequestPermission -> {
                intent { postSideEffect(PermissionContract.SideEffect.OpenPermissionDialog) }
            }

            PermissionContract.Intent.OpenMusicListScreen -> {
                intent { direction.openMusicListScreen() }
            }
        }
    }
}





