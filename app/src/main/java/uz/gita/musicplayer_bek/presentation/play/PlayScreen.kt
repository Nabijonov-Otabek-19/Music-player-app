package uz.gita.musicplayer_bek.presentation.play

import android.content.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.musicplayer_bek.R
import uz.gita.musicplayer_bek.data.model.ActionEnum
import uz.gita.musicplayer_bek.data.model.CommandEnum
import uz.gita.musicplayer_bek.ui.theme.MusicPlayerTheme
import uz.gita.musicplayer_bek.utils.MyEventBus
import uz.gita.musicplayer_bek.utils.base.getMusicDataByPosition
import uz.gita.musicplayer_bek.utils.base.getTime
import uz.gita.musicplayer_bek.utils.base.startMusicService
import uz.gita.musicplayer_bek.utils.logger
import java.util.concurrent.TimeUnit

class PlayScreen : AndroidScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel: PlayContract.ViewModel = getViewModel<PlayViewModel>()
        val uiState = viewModel.collectAsState()

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
                Scaffold(topBar = { TopBar() }) {
                    PlayScreenContent(
                        uiState,
                        viewModel::onEventDispatcher,
                        modifier = Modifier.padding(it)
                    )
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier.clickable {
                    logger("Back to Music List")
                }
            )
        }
    }
}

@Composable
fun PlayScreenContent(
    uiState: State<PlayContract.UIState>,
    eventListener: (PlayContract.Intent) -> Unit,
    modifier: Modifier
) {

    val musicData = MyEventBus.currentMusicData.collectAsState(
        initial = MyEventBus.cursor!!.getMusicDataByPosition(MyEventBus.selectMusicPos)
    )

    val seekBarState = MyEventBus.currentTimeFlow.collectAsState(initial = 0)
    var seekBarValue by remember { mutableStateOf(seekBarState.value) }
    val mucisIsPlaying = MyEventBus.isPlaying.collectAsState()

    val milliseconds = musicData.value!!.duration
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    val minutes = (milliseconds / 1000 / 60) % 60
    val seconds = (milliseconds / 1000) % 60

    val duration = if (hours == 0L) "%02d:%02d".format(minutes, seconds)
    else "%02d:%02d:%02d".format(hours, minutes, seconds) // 03:45

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
                    .padding(top = 70.dp)
                    .background(Color(0XFF988E8E), RoundedCornerShape(4.dp))
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_music_disk),
                contentDescription = null
            )

            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = musicData.value!!.title ?: "Unknown",
                fontSize = 24.sp
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
                    logger("Slider = onValueChangeFinished")
                },
                valueRange = 0f..musicData.value!!.duration.toFloat(),
                steps = 1000,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFE70A0A),
                    activeTickColor = Color(0xFFE70A0A),
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
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .clickable {
                            eventListener.invoke(PlayContract.Intent.UserAction(ActionEnum.PREV))
                            seekBarValue = 0
                        },
                    painter = painterResource(id = R.drawable.previous),
                    contentDescription = null
                )

                Image(
                    modifier = Modifier
                        .size(60.dp)
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
                        .size(60.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .clickable {
                            eventListener.invoke(PlayContract.Intent.UserAction(ActionEnum.NEXT))
                            seekBarValue = 0
                        },
                    painter = painterResource(id = R.drawable.previous),
                    contentDescription = null
                )
            }
        }
    }
}