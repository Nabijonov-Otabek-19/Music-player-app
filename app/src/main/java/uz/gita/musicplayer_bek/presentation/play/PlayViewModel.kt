package uz.gita.musicplayer_bek.presentation.play

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor() :
    ViewModel(), PlayContract.ViewModel {

    override val container =
        container<PlayContract.UIState, PlayContract.SideEffect>(PlayContract.UIState.InitState)


    override fun onEventDispatcher(intent: PlayContract.Intent) {
        when (intent) {
            is PlayContract.Intent.UserAction -> {
                intent { postSideEffect(PlayContract.SideEffect.UserAction(intent.actionEnum)) }
            }
        }
    }
}