package uz.gita.musicplayer_bek.presentation.addplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.musicplayer_bek.domain.repository.AppRepository
import javax.inject.Inject

@HiltViewModel
class AddPlaylistViewModel @Inject constructor(
    private val direction: AddPlaylistDirection,
    private val repository: AppRepository
) : AddPlaylistContract.ViewModel, ViewModel() {

    override val container =
        container<AddPlaylistContract.UIState, AddPlaylistContract.SideEffect>(AddPlaylistContract.UIState.Loading)

    override fun onEventDispatcher(intent: AddPlaylistContract.Intent) {
        when (intent) {
            AddPlaylistContract.Intent.OpenPlaylistScreen -> {
                viewModelScope.launch {
                    direction.navigateToPlaylistScreen()
                }
            }

            AddPlaylistContract.Intent.LoadPlayLists -> {
                repository.getAllPlaylists().onEach {
                    intent { reduce { AddPlaylistContract.UIState.PreparedData(it) } }
                }.launchIn(viewModelScope)
            }

            is AddPlaylistContract.Intent.AddPlayList -> {
                repository.addPlayList(intent.playListData)
            }
        }
    }
}