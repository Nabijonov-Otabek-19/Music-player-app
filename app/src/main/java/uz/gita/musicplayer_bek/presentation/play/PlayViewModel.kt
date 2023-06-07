package uz.gita.musicplayer_bek.presentation.play

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor() :
    ViewModel(), PlayContract.ViewModel {

    override val container
    = container<PlayContract.UIState, Nothing>(PlayContract.UIState.Manage)


    override fun onEventDispatcher(intent: PlayContract.Intent) {
        when (intent) {
            PlayContract.Intent.Manage -> {
                intent { reduce { PlayContract.UIState.Manage } }
            }

            PlayContract.Intent.Next -> {
                intent { reduce { PlayContract.UIState.Next } }
            }

            PlayContract.Intent.Prev -> {
                intent { reduce { PlayContract.UIState.Prev } }
            }
        }
    }
}