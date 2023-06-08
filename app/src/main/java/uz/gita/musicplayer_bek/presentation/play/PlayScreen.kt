package uz.gita.musicplayer_bek.presentation.play

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.musicplayer_bek.R
import uz.gita.musicplayer_bek.data.model.ActionEnum
import uz.gita.musicplayer_bek.data.model.CommandEnum
import uz.gita.musicplayer_bek.service.MusicService
import uz.gita.musicplayer_bek.ui.theme.MusicPlayerTheme
import uz.gita.musicplayer_bek.utils.MyEventBus
import uz.gita.musicplayer_bek.utils.base.getMusicDataByPosition
import uz.gita.musicplayer_bek.utils.logger
import java.util.concurrent.TimeUnit

class PlayScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel: PlayContract.ViewModel = getViewModel<PlayViewModel>()

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
                val uiState = viewModel.collectAsState().value
                PlayScreenContent(
                    uiState, viewModel::onEventDispatcher
                )
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
}

private fun getTime(time: Int): String {
    val hour = time / 3600
    val minute = (time % 3600) / 60
    val second = time % 60

    val hourText = if (hour > 0) {
        if (hour < 10) "0$hour:"
        else "$hour:"
    } else ""

    val minuteText = if (minute < 10) "0$minute:"
    else "$minute:"

    val secondText = if (second < 10) "0$second"
    else "$second"

    return "$hourText$minuteText$secondText"
}

@Composable
fun PlayScreenContent(
    uiState: PlayContract.UIState,
    eventListener: (PlayContract.Intent) -> Unit
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

    when (uiState) {
        PlayContract.UIState.UpdateState -> {
            logger("PlayScreen = UpdateState")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(0.dp)
                .weight(1.6f)
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
                value = seekBarValue.toFloat(),
                onValueChange = { newState -> seekBarValue = newState.toInt() },
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
            Row(modifier = Modifier.fillMaxWidth()) {
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
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    modifier = Modifier
                        .rotate(180f)
                        .width(0.dp)
                        .weight(1f)
                        .size(70.dp)
                        .clip(CircleShape)
                        .clickable {
                            eventListener.invoke(PlayContract.Intent.UserAction(ActionEnum.PREV))
                            seekBarValue = 0},
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null
                )

                Image(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f)
                        .size(70.dp)
                        .clip(CircleShape)
                        .clickable { eventListener.invoke(PlayContract.Intent.UserAction(ActionEnum.MANAGE)) },
                    painter = painterResource(
                        id = if (mucisIsPlaying.value) R.drawable.ic_pause
                        else R.drawable.ic_play
                    ),
                    contentDescription = null
                )

                Image(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f)
                        .size(70.dp)
                        .clip(CircleShape)
                        .clickable {
                            eventListener.invoke(PlayContract.Intent.UserAction(ActionEnum.NEXT))
                            seekBarValue = 0
                                   },
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null
                )
            }
        }
    }
}