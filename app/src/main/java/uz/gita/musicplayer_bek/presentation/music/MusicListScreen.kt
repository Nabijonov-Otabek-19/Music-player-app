package uz.gita.musicplayer_bek.presentation.music

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.musicplayer_bek.MainActivity
import uz.gita.musicplayer_bek.R
import uz.gita.musicplayer_bek.data.model.CommandEnum
import uz.gita.musicplayer_bek.ui.component.CurrentMusicItemComponent
import uz.gita.musicplayer_bek.ui.component.LoadingComponent
import uz.gita.musicplayer_bek.ui.component.MusicItemComponent
import uz.gita.musicplayer_bek.ui.theme.MusicPlayerTheme
import uz.gita.musicplayer_bek.utils.MyEventBus
import uz.gita.musicplayer_bek.utils.base.getMusicDataByPosition
import uz.gita.musicplayer_bek.utils.base.startMusicService

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
                    startMusicService(activity, CommandEnum.PLAY)
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(56.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .width(0.dp)
                .weight(1f),
            fontSize = 22.sp,
            text = "Music List"
        )

        Image(painter = painterResource(id = R.drawable.ic_search), contentDescription = null)
    }
}