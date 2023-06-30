package uz.gita.musicplayer_bek.presentation.addplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AddPlaylistViewModel @Inject constructor(
    private val direction: AddPlaylistDirection
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
        }
    }
}