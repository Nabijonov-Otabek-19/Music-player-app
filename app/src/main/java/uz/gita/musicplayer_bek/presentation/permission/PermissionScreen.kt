package uz.gita.musicplayer_bek.presentation.permission

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.musicplayer_bek.ui.theme.MusicPlayerTheme
import uz.gita.musicplayer_bek.utils.base.checkPermissions

class PermissionScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel: PermissionContract.ViewModel = getViewModel<PermissionViewModel>()

        viewModel.collectSideEffect(lifecycleState = Lifecycle.State.CREATED) { sideEffect ->
            when (sideEffect) {
                PermissionContract.SideEffect.OpenPermissionDialog -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        context.checkPermissions(
                            arrayListOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.POST_NOTIFICATIONS,
                                Manifest.permission.READ_MEDIA_AUDIO
                            )
                        ) {
                            viewModel.onEventDispatcher(PermissionContract.Intent.OpenMusicListScreen)
                        }
                    } else {
                        context.checkPermissions(arrayListOf(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            viewModel.onEventDispatcher(PermissionContract.Intent.OpenMusicListScreen)
                        }
                    }
                }
            }
        }

        MusicPlayerTheme {
            val uiState = viewModel.collectAsState().value
            PermissionContent(uiState, viewModel::onEventDispatcher)
        }
    }
}

@Composable
private fun PermissionContent(
    uiState: PermissionContract.UIState,
    eventDispatcher: (PermissionContract.Intent) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = { eventDispatcher.invoke(PermissionContract.Intent.RequestPermission) }
            ) {
                Text(text = "Request permission")
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun PermissionContentPreview() {
    MusicPlayerTheme {
        PermissionContent(PermissionContract.UIState.InitState) {}
    }
}