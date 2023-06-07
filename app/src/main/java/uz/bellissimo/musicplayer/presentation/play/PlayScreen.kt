package uz.bellissimo.musicplayer.presentation.play

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import uz.bellissimo.musicplayer.MainActivity
import uz.bellissimo.musicplayer.R
import uz.bellissimo.musicplayer.data.model.CommandEnum
import uz.bellissimo.musicplayer.data.model.MusicData
import uz.bellissimo.musicplayer.service.MusicService
import uz.bellissimo.musicplayer.ui.component.LoadingComponent
import uz.bellissimo.musicplayer.ui.theme.MusicPlayerTheme
import uz.bellissimo.musicplayer.utils.MyEventBus
import uz.bellissimo.musicplayer.utils.base.getMusicDataByPosition
import uz.bellissimo.musicplayer.utils.logger
import java.util.concurrent.TimeUnit

class PlayScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        MusicPlayerTheme {
            Surface(color = MaterialTheme.colorScheme.background) {
                val viewModel: PlayContract.ViewModel = getViewModel<PlayViewModel>()
                val uiState = viewModel.collectAsState().value
                PlayScreenContent(
                    uiState, viewModel::onEventDispatcher
                )
            }
        }
    }
}

@Composable
fun PlayScreenContent(
    uiState: PlayContract.UIState,
    eventListener: (PlayContract.Intent) -> Unit
) {

    val activity = LocalContext.current as MainActivity
    val intent = Intent(activity, MusicService::class.java)

    var seekBarState by remember { mutableStateOf(0f) }

    val musicData by remember { mutableStateOf(MyEventBus.cursor!!.getMusicDataByPosition(MyEventBus.selectMusicPos)) }

    val milliseconds = musicData.duration
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    val minutes = (milliseconds / 1000 / 60) % 60
    val seconds = (milliseconds / 1000) % 60

    val duration = if (hours == 0L) "%02d:%02d".format(minutes, seconds)
    else "%02d:%02d:%02d".format(hours, minutes, seconds)

    when (uiState) {
        PlayContract.UIState.Manage -> {
            logger("PlayScreen = Manage")
        }

        PlayContract.UIState.Next -> {
            logger("PlayScreen = Next")
            intent.putExtra("COMMAND", CommandEnum.NEXT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity.startForegroundService(intent)
            } else activity.startService(intent)
        }

        PlayContract.UIState.Prev -> {
            if (MyEventBus.selectMusicPos - 1 == -1) {
                MyEventBus.selectMusicPos = MyEventBus.cursor!!.count - 1
            } else {
                --MyEventBus.selectMusicPos
            }
            logger("PlayScreen = Prev")
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
                    .background(color = Color.Gray)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_music_disk),
                contentDescription = null
            )

            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = musicData.title ?: "Unknown",
                fontSize = 24.sp
            )

            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                text = musicData.artist ?: "Unknown",
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
                value = seekBarState,
                onValueChange = { newState ->
                    seekBarState = newState
                    //  val seekDuration = (musicData.duration * newState).toInt()
//                onUpdatePosition?.invoke(seekDuration)
                },
                onValueChangeFinished = {
                    // val seekDuration = (musicData.duration * seekBarState).toInt()
//                mediaPlayer?.seekTo(seekDuration)
                }
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f),
                    text = "00:00"
                )
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
                        .clickable { eventListener.invoke(PlayContract.Intent.Prev) },
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null
                )

                Image(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f)
                        .size(70.dp)
                        .clickable { eventListener.invoke(PlayContract.Intent.Manage) },
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = null
                )

                Image(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f)
                        .size(70.dp)
                        .clickable { eventListener.invoke(PlayContract.Intent.Next) },
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null
                )
            }
        }
    }
}