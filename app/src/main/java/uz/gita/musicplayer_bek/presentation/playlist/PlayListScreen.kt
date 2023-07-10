package uz.gita.musicplayer_bek.presentation.playlist

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.*
import uz.gita.musicplayer_bek.MainActivity
import uz.gita.musicplayer_bek.R
import uz.gita.musicplayer_bek.data.model.CommandEnum
import uz.gita.musicplayer_bek.navigation.AppScreen
import uz.gita.musicplayer_bek.ui.component.LoadingComponent
import uz.gita.musicplayer_bek.ui.component.MusicItemComponent
import uz.gita.musicplayer_bek.ui.theme.Light_Red
import uz.gita.musicplayer_bek.ui.theme.MusicPlayerTheme
import uz.gita.musicplayer_bek.utils.MyEventBus
import uz.gita.musicplayer_bek.utils.base.checkMusicExistance
import uz.gita.musicplayer_bek.utils.base.getMusicDataByPosition
import uz.gita.musicplayer_bek.utils.base.startMusicService
import uz.gita.musicplayer_bek.utils.logger

class PlayListScreen : AppScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val activity = LocalContext.current as MainActivity
        val viewModel: PlayListContract.ViewModel = getViewModel<PlayListViewModel>()
        val uiState = viewModel.collectAsState()

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                PlayListContract.SideEffect.StartMusicService -> {
                    startMusicService(activity, CommandEnum.PLAY)
                }
            }
        }

        LifecycleEffect(
            onStarted = { viewModel.onEventDispatcher(PlayListContract.Intent.CheckMusicExistance) }
        )

        MusicPlayerTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                Scaffold(topBar = { TopBar() }) {
                    PlayListScreenContent(
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
fun PlayListScreenContent(
    uiState: State<PlayListContract.UIState>,
    eventListener: (PlayListContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier.fillMaxSize()) {
        when (uiState.value) {

            PlayListContract.UIState.IsExistMusic -> {
                LoadingComponent()
                for (pos in 0 until MyEventBus.cursor!!.count) {
                    val data = MyEventBus.cursor!!.getMusicDataByPosition(pos)
                    logger("Room loop = ${data.title}")
                    if (!checkMusicExistance(data)) {
                        eventListener.invoke(PlayListContract.Intent.DeleteMusic(data))
                    }
                }
                eventListener.invoke(PlayListContract.Intent.LoadMusics)
            }

            PlayListContract.UIState.Loading -> {
                eventListener.invoke(PlayListContract.Intent.CheckMusicExistance)
            }

            is PlayListContract.UIState.PreparedData -> {
                if (MyEventBus.cursor!!.count == 0) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(180.dp),
                        painter = painterResource(id = R.drawable.ic_no_music),
                        contentDescription = null
                    )
                } else {
                    LazyColumn {
                        for (pos in 0 until MyEventBus.cursor!!.count) {
                            item {
                                MusicItemComponent(
                                    musicData = MyEventBus.cursor!!.getMusicDataByPosition(pos),
                                    onClick = {
                                        MyEventBus.selectMusicPos = pos
                                        eventListener.invoke(PlayListContract.Intent.PlayMusic)
                                        eventListener.invoke(PlayListContract.Intent.OpenPlayScreen)
                                    },
                                    onLongClick = {}
                                )
                            }
                        }
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
            .background(color = Light_Red)
            .height(56.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            color = Color.Black,
            fontSize = 22.sp,
            text = "My playlist",
            fontWeight = FontWeight.Bold
        )
    }
}