package uz.gita.musicplayer_bek.presentation.playlist

import android.Manifest
import android.os.Build
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.*
import uz.gita.musicplayer_bek.MainActivity
import uz.gita.musicplayer_bek.R
import uz.gita.musicplayer_bek.data.model.CommandEnum
import uz.gita.musicplayer_bek.navigation.AppScreen
import uz.gita.musicplayer_bek.ui.component.CurrentMusicItemComponent
import uz.gita.musicplayer_bek.ui.component.LoadingComponent
import uz.gita.musicplayer_bek.ui.component.MusicItemComponent
import uz.gita.musicplayer_bek.ui.theme.MusicPlayerTheme
import uz.gita.musicplayer_bek.utils.MyEventBus
import uz.gita.musicplayer_bek.utils.base.checkPermissions
import uz.gita.musicplayer_bek.utils.base.getMusicDataByPosition
import uz.gita.musicplayer_bek.utils.base.startMusicService

class PlayListScreen : AppScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val activity = LocalContext.current as MainActivity
        val viewModel: PlayListContract.ViewModel = getViewModel<PlayListViewModel>()
        val uiState = viewModel.collectAsState()

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

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                PlayListContract.SideEffect.StartMusicService -> {
                    startMusicService(activity, CommandEnum.PLAY)
                }

                PlayListContract.SideEffect.OpenPermissionDialog -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        activity.checkPermissions(
                            arrayListOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.POST_NOTIFICATIONS,
                                Manifest.permission.READ_MEDIA_AUDIO,
                                Manifest.permission.READ_PHONE_STATE
                            )
                        ) {
                            viewModel.onEventDispatcher(PlayListContract.Intent.LoadMusics)
                        }
                    } else {
                        activity.checkPermissions(
                            arrayListOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE
                            )
                        ) {
                            viewModel.onEventDispatcher(PlayListContract.Intent.LoadMusics)
                        }
                    }
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
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        when (uiState.value) {
            PlayListContract.UIState.Loading -> {
                LoadingComponent()
                eventListener.invoke(PlayListContract.Intent.LoadMusics)
            }

            is PlayListContract.UIState.PreparedData -> {
                val data = (uiState.value as PlayListContract.UIState.PreparedData).savedMusics

                if (data.isEmpty()) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(180.dp),
                        painter = painterResource(id = R.drawable.ic_no_music),
                        contentDescription = null
                    )

                } else {
                    LazyColumn {
                        items(data.size) {
                            MusicItemComponent(
                                musicData = data[it],
                                onClick = {
                                    MyEventBus.selectMusicPos = data[it].storagePosition
                                    eventListener.invoke(PlayListContract.Intent.PlayMusic)
                                    eventListener.invoke(PlayListContract.Intent.OpenPlayScreen)
                                },
                                onLongClick = {
                                    eventListener(PlayListContract.Intent.DeleteMusic(data[it]))
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
                        onClick = { eventListener.invoke(PlayListContract.Intent.OpenPlayScreen) },
                        onClickManage = { startMusicService(context, CommandEnum.MANAGE) })
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
            .height(56.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            fontSize = 22.sp,
            text = "My playlist"
        )
    }
}