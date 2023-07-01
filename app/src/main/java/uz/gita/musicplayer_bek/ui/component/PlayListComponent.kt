package uz.gita.musicplayer_bek.ui.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import uz.gita.musicplayer_bek.R
import uz.gita.musicplayer_bek.data.model.PlayListData

@Composable
fun PlayListComponent(playListData: PlayListData) {
    Card(shape = CardDefaults.outlinedShape) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                modifier = Modifier.size(70.dp),
                painter = painterResource(id = R.drawable.ic_music),
                contentDescription = null
            )

            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp),
                text = playListData.name,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PlayListComponentPreview() {
    PlayListComponent(PlayListData(0, "Retro"))
}