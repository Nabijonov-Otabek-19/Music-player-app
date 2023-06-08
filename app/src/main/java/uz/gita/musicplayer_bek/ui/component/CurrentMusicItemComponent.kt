package uz.gita.musicplayer_bek.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import uz.gita.musicplayer_bek.R
import uz.gita.musicplayer_bek.data.model.MusicData
import uz.gita.musicplayer_bek.ui.theme.MusicPlayerTheme
import uz.gita.musicplayer_bek.utils.MyEventBus

@OptIn(ExperimentalUnitApi::class)
@Composable
fun CurrentMusicItemComponent(
    modifier: Modifier = Modifier,
    musicData: MusicData,
    onClick: () -> Unit,
    onClickManage: () -> Unit
) {
    val mucisIsPlaying = MyEventBus.isPlaying.collectAsState()

    Surface(
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier
            .padding(8.dp)
            .wrapContentHeight()
            .clip(shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondary)
            .clickable { onClick.invoke() }
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(4.dp)
        )
        {
            Image(
                painter = painterResource(id = R.drawable.ic_music_disk),
                contentDescription = "MusicDisk",
                modifier = Modifier
                    .width(56.dp)
                    .height(56.dp)
                    .background(Color(0XFF988E8E), RoundedCornerShape(8.dp))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = musicData.title ?: "Unknown name",
                    color = Color.Black,
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = musicData.artist ?: "Unknown artist",
                    color = Color(0XFF988E8E),
                    fontSize = TextUnit(14f, TextUnitType.Sp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            Image(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .clickable { onClickManage.invoke() },
                painter = painterResource(
                    id = if (mucisIsPlaying.value) R.drawable.ic_pause
                    else R.drawable.ic_play
                ),
                contentDescription = null
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CurrentMusicItemComponentPreview() {
    MusicPlayerTheme {
        val musicDate = MusicData(
            0,
            "My artist",
            "Test title",
            null,
            10000
        )

        CurrentMusicItemComponent(
            musicData = musicDate,
            onClick = {},
            onClickManage = {}
        )
    }
}



