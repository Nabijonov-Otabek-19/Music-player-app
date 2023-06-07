package uz.bellissimo.musicplayer.presentation.music

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.bellissimo.musicplayer.MainActivity
import uz.bellissimo.musicplayer.data.model.CommandEnum
import uz.bellissimo.musicplayer.service.MusicService
import uz.bellissimo.musicplayer.ui.component.LoadingComponent
import uz.bellissimo.musicplayer.ui.component.MusicItemComponent
import uz.bellissimo.musicplayer.ui.theme.MusicPlayerTheme
import uz.bellissimo.musicplayer.utils.MyEventBus
import uz.bellissimo.musicplayer.utils.base.getMusicDataByPosition

class MusicListScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val activity = LocalContext.current as MainActivity
        val viewModel: MusicListContract.ViewModel = getViewModel<MusicListViewModel>()

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                MusicListContract.SideEffect.StartMusicService -> {
                    val intent = Intent(activity, MusicService::class.java)
                    intent.putExtra("COMMAND", CommandEnum.PLAY)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        activity.startForegroundService(intent)
                    } else activity.startService(intent)
                }
            }
        }

        MusicPlayerTheme {
            val uiState = viewModel.collectAsState().value
            MusicListContent(uiState = uiState, eventListener = viewModel::onEventDispatcher)
        }
    }
}

@Composable
private fun MusicListContent(
    uiState: MusicListContract.UIState,
    eventListener: (MusicListContract.Intent) -> Unit
) {
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize()) {
        when (uiState) {

            MusicListContract.UIState.Loading -> {
                LoadingComponent()
                eventListener.invoke(MusicListContract.Intent.LoadMusics(context))
            }

            MusicListContract.UIState.PreparedData -> {
                LazyColumn {
                    for (pos in 0 until MyEventBus.cursor!!.count) {
                        item {
                            MusicItemComponent(
                                musicData = MyEventBus.cursor!!.getMusicDataByPosition(pos),
                                onClick = {
                                    MyEventBus.selectMusicPos = pos
                                    eventListener.invoke(MusicListContract.Intent.PlayMusic)
                                    eventListener.invoke(MusicListContract.Intent.OpenPlayScreen)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}