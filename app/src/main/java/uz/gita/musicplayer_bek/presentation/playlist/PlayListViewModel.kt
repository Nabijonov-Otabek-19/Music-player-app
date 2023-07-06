package uz.gita.musicplayer_bek.presentation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.musicplayer_bek.domain.repository.AppRepository
import javax.inject.Inject

@HiltViewModel
class PlayListViewModel @Inject constructor(
    private val direction: PlayListDirection,
    private val appRepository: AppRepository
) : ViewModel(), PlayListContract.ViewModel {

    override val container =
        container<PlayListContract.UIState, PlayListContract.SideEffect>(PlayListContract.UIState.Loading)


    override fun onEventDispatcher(intent: PlayListContract.Intent) {
        when (intent) {
            PlayListContract.Intent.LoadMusics -> {
                appRepository.getAllMusics().onEach { list ->
                    intent { reduce { PlayListContract.UIState.PreparedData(list) } }
                }.launchIn(viewModelScope)
            }

            is PlayListContract.Intent.DeleteMusic -> {
                appRepository.deleteMusic(intent.musicData)
            }

            PlayListContract.Intent.PlayMusic -> {
                intent { postSideEffect(PlayListContract.SideEffect.StartMusicService) }
            }

            PlayListContract.Intent.OpenPlayScreen -> {
                viewModelScope.launch { direction.navigateToPlayScreen() }
            }

            PlayListContract.Intent.RequestPermission -> {
                intent { postSideEffect(PlayListContract.SideEffect.OpenPermissionDialog) }
            }
        }
    }
}