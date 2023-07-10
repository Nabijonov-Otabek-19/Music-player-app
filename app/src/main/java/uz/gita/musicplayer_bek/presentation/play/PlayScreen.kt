package uz.gita.musicplayer_bek.presentation.play

import android.content.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.*
import uz.gita.musicplayer_bek.R
import uz.gita.musicplayer_bek.data.model.ActionEnum
import uz.gita.musicplayer_bek.data.model.CommandEnum
import uz.gita.musicplayer_bek.data.model.CursorEnum
import uz.gita.musicplayer_bek.data.model.MusicData
import uz.gita.musicplayer_bek.navigation.AppScreen
import uz.gita.musicplayer_bek.ui.theme.Light_Red
import uz.gita.musicplayer_bek.ui.theme.MusicPlayerTheme
import uz.gita.musicplayer_bek.ui.theme.Red
import uz.gita.musicplayer_bek.utils.MyEventBus
import uz.gita.musicplayer_bek.utils.base.getMusicDataByPosition
import uz.gita.musicplayer_bek.utils.base.getTime
import uz.gita.musicplayer_bek.utils.base.startMusicService
import uz.gita.musicplayer_bek.utils.toast
import java.util.concurrent.TimeUnit

class PlayScreen : AppScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel: PlayContract.ViewModel = getViewModel<PlayViewModel>()
        val uiState = viewModel.collectAsState()

        val musicData = MyEventBus.currentMusicData.collectAsState(
            initial = if (MyEventBus.currentCursorEnum == CursorEnum.SAVED)
                MyEventBus.roomCursor!!.getMusicDataByPosition(MyEventBus.roomPos)
            else MyEventBus.storageCursor!!.getMusicDataByPosition(MyEventBus.storagePos)
        )

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is PlayContract.SideEffect.UserAction -> {
                    when (sideEffect.actionEnum) {
                        ActionEnum.MANAGE -> {
                            startMusicService(context, CommandEnum.MANAGE)
                        }

                        ActionEnum.NEXT -> {
                            startMusicService(context, CommandEnum.NEXT)
                        }

                        ActionEnum.PREV -> {
                            startMusicService(context, CommandEnum.PREV)
                        }

                        ActionEnum.UPDATE_SEEKBAR -> {
                            startMusicService(context, CommandEnum.UPDATE_SEEKBAR)
                        }
                    }
                }
            }
        }

        MusicPlayerTheme {
            Surface(color = MaterialTheme.colorScheme.background) {
                Scaffold(
                    topBar = { TopBar(musicData, uiState, viewModel::onEventDispatcher) }) {
                    PlayScreenContent(
                        musicData,
                        viewModel::onEventDispatcher,
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}

@Composable
fun TopBar(
    musicData: State<MusicData?>,
    uiState: State<PlayContract.UIState>,
    onEventDispatcher: (PlayContract.Intent) -> Unit
) {

    val context = LocalContext.current
    onEventDispatcher(PlayContract.Intent.CheckMusic(musicData.value!!))

    when (uiState.value) {
        is PlayContract.UIState.CheckMusic -> {
            val isSaved = (uiState.value as PlayContract.UIState.CheckMusic).isSaved
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Light_Red)
                    .height(56.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    modifier = Modifier.padding(start = 12.dp),
                    onClick = { onEventDispatcher(PlayContract.Intent.Back) }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null
                    )
                }

                IconButton(
                    modifier = Modifier.padding(end = 12.dp),
                    onClick = {
                        if (isSaved) {
                            onEventDispatcher.invoke(
                                PlayContract.Intent.DeleteMusic(
                                    MusicData(
                                        musicData.value!!.id,
                                        musicData.value!!.artist,
                                        musicData.value!!.title,
                                        musicData.value!!.data,
                                        musicData.value!!.duration
                                    )
                                )
                            )
                            toast(context, "Music Removed")

                        } else {
                            onEventDispatcher.invoke(
                                PlayContract.Intent.SaveMusic(
                                    MusicData(
                                        musicData.value!!.id,
                                        musicData.value!!.artist,
                                        musicData.value!!.title,
                                        musicData.value!!.data,
                                        musicData.value!!.duration
                                    )
                                )
                            )
                            toast(context, "Music Saved")
                        }

                        onEventDispatcher.invoke(PlayContract.Intent.CheckMusic(musicData.value!!))
                    }
                ) {
                    Image(
                        painter = painterResource(id = if (isSaved) R.drawable.ic_fav else R.drawable.ic_not_fav),
                        contentDescription = null
                    )
                }
            }
        }

        else -> {}
    }
}

@Composable
fun PlayScreenContent(
    musicData: State<MusicData?>,
    eventListener: (PlayContract.Intent) -> Unit,
    modifier: Modifier
) {

    val seekBarState = MyEventBus.currentTimeFlow.collectAsState(initial = 0)
    var seekBarValue by remember { mutableStateOf(seekBarState.value) }
    val mucisIsPlaying = MyEventBus.isPlaying.collectAsState()

    val milliseconds = musicData.value!!.duration
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    val minutes = (milliseconds / 1000 / 60) % 60
    val seconds = (milliseconds / 1000) % 60

    val duration = if (hours == 0L) "%02d:%02d".format(minutes, seconds) // 03:45
    else "%02d:%02d:%02d".format(hours, minutes, seconds) // 01:03:45

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(0.dp)
                .weight(1.7f)
        ) {
            Image(
                modifier = Modifier
                    .size(250.dp)
                    .padding(top = 10.dp, bottom = 12.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_play_screen),
                contentDescription = null
            )

            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = musicData.value!!.title ?: "Unknown",
                fontSize = 20.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                text = musicData.value!!.artist ?: "Unknown",
                fontSize = 18.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.dp)
                .weight(1f)
        ) {
            Slider(
                value = seekBarState.value.toFloat(),
                onValueChange = { newState ->
                    seekBarValue = newState.toInt()
                    eventListener.invoke(PlayContract.Intent.UserAction(ActionEnum.UPDATE_SEEKBAR))
                },
                onValueChangeFinished = {
                    MyEventBus.currentTime.value = seekBarValue
                    eventListener.invoke(PlayContract.Intent.UserAction(ActionEnum.UPDATE_SEEKBAR))
                },
                valueRange = 0f..musicData.value!!.duration.toFloat(),
                steps = 1000,
                colors = SliderDefaults.colors(
                    thumbColor = Red,
                    activeTickColor = Red,
                    activeTrackColor = Color(0xFFCCC2C2)
                )
            )

            // 00:00
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f),
                    text = getTime(seekBarState.value / 1000)
                )
                // 03:45
                Text(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f),
                    textAlign = TextAlign.End,
                    text = duration
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .clickable {
                            eventListener.invoke(PlayContract.Intent.UserAction(ActionEnum.PREV))
                            seekBarValue = 0
                            eventListener(PlayContract.Intent.CheckMusic(musicData.value!!))
                        },
                    painter = painterResource(id = R.drawable.previous),
                    contentDescription = null
                )

                Image(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .clickable { eventListener.invoke(PlayContract.Intent.UserAction(ActionEnum.MANAGE)) },
                    painter = painterResource(
                        id = if (mucisIsPlaying.value) R.drawable.pause_button
                        else R.drawable.play_button
                    ),
                    contentDescription = null
                )

                Image(
                    modifier = Modifier
                        .rotate(180f)
                        .size(50.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .clickable {
                            eventListener.invoke(PlayContract.Intent.UserAction(ActionEnum.NEXT))
                            seekBarValue = 0
                            eventListener(PlayContract.Intent.CheckMusic(musicData.value!!))
                        },
                    painter = painterResource(id = R.drawable.previous),
                    contentDescription = null
                )
            }
        }
    }
}