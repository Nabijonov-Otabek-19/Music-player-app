package uz.bellissimo.musicplayer.presentation.play

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import cafe.adriel.voyager.androidx.AndroidScreen
import uz.bellissimo.musicplayer.R
import uz.bellissimo.musicplayer.data.model.MusicData
import uz.bellissimo.musicplayer.ui.theme.MusicPlayerTheme
import uz.bellissimo.musicplayer.utils.MyEventBus
import uz.bellissimo.musicplayer.utils.base.getMusicDataByPosition

class PlayScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        MusicPlayerTheme {
            Surface(color = MaterialTheme.colorScheme.background) {
                PlayScreenContent(MyEventBus.cursor!!.getMusicDataByPosition(MyEventBus.selectMusicPos))
            }
        }
    }
}

@Composable
fun PlayScreenContent(musicData: MusicData) {

    var seekBarState by remember { mutableStateOf(0f) }

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
                    text = musicData.duration.toString()
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
                        .clickable { },
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null
                )

                Image(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f)
                        .size(70.dp)
                        .clickable { },
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = null
                )

                Image(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f)
                        .size(70.dp)
                        .clickable { },
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null
                )
            }
        }
    }
}

//@Preview(showSystemUi = true)
@Composable
fun PlayScreenContentPreview() {
    MusicPlayerTheme {
        PlayScreenContent(MusicData(0, "Unknown", "Unknown", "Unknown", "00:00".toLong()))
    }
}