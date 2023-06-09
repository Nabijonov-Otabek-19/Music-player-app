package uz.gita.musicplayer_bek.presentation.music

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.musicplayer_bek.MainActivity
import uz.gita.musicplayer_bek.data.model.CommandEnum
import uz.gita.musicplayer_bek.service.MusicService
import uz.gita.musicplayer_bek.ui.component.CurrentMusicItemComponent
import uz.gita.musicplayer_bek.ui.component.LoadingComponent
import uz.gita.musicplayer_bek.ui.component.MusicItemComponent
import uz.gita.musicplayer_bek.ui.theme.Light_Red
import uz.gita.musicplayer_bek.ui.theme.MusicPlayerTheme
import uz.gita.musicplayer_bek.utils.MyEventBus
import uz.gita.musicplayer_bek.utils.base.getMusicDataByPosition

class MusicListScreen : AndroidScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val activity = LocalContext.current as MainActivity
        val viewModel: MusicListContract.ViewModel = getViewModel<MusicListViewModel>()
        val uiState = viewModel.collectAsState()

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
            Surface(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    topBar = { TopBar() }
                ) {
                    MusicListContent(
                        uiState = uiState,
                        eventListener = viewModel::onEventDispatcher,
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}

private fun startMusicService(context: Context, commandEnum: CommandEnum) {
    val intent = Intent(context, MusicService::class.java)
    intent.putExtra("COMMAND", commandEnum)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else context.startService(intent)
}


@Composable
private fun MusicListContent(
    uiState: State<MusicListContract.UIState>,
    eventListener: (MusicListContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Surface(modifier = modifier.fillMaxSize()) {
        Box {
            when (uiState.value) {
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

                    if (MyEventBus.selectMusicPos != -1) {
                        CurrentMusicItemComponent(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            musicData = MyEventBus.cursor!!.getMusicDataByPosition(
                                MyEventBus.selectMusicPos
                            ),
                            onClick = { eventListener.invoke(MusicListContract.Intent.OpenPlayScreen) },
                            onClickManage = { startMusicService(context, CommandEnum.MANAGE) })
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = Light_Red),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(fontSize = 22.sp, text = "Music List")
    }
}