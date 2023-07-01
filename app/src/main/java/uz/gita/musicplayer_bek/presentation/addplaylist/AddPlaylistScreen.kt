package uz.gita.musicplayer_bek.presentation.addplaylist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.musicplayer_bek.navigation.AppScreen
import uz.gita.musicplayer_bek.ui.component.LoadingComponent
import uz.gita.musicplayer_bek.ui.component.PlayListComponent
import uz.gita.musicplayer_bek.ui.theme.MusicPlayerTheme

class AddPlaylistScreen : AppScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val viewModel: AddPlaylistContract.ViewModel = getViewModel<AddPlaylistViewModel>()
        val uiState = viewModel.collectAsState()

        MusicPlayerTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    topBar = { }
                ) {
                    AddPlaylistScreenContent(
                        uiState = uiState,
                        onEventDispatcher = viewModel::onEventDispatcher,
                        Modifier.padding(it)
                    )
                }
            }
        }
    }
}

@Composable
fun AddPlaylistScreenContent(
    uiState: State<AddPlaylistContract.UIState>,
    onEventDispatcher: (AddPlaylistContract.Intent) -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {

        when (uiState.value) {
            AddPlaylistContract.UIState.Loading -> {
                LoadingComponent()
                onEventDispatcher.invoke(AddPlaylistContract.Intent.LoadPlayLists)
            }

            is AddPlaylistContract.UIState.PreparedData -> {
                val data = (uiState.value as AddPlaylistContract.UIState.PreparedData).playListData
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    content = {
                        items(data.size) {
                            Spacer(modifier = Modifier.size(8.dp))

                            PlayListComponent(playListData = data[it])
                        }
                    })
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomEnd),
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.Blue,
            onClick = {
                onEventDispatcher(AddPlaylistContract.Intent.OpenPlaylistScreen)
            }) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
        }
    }
}