package uz.bellissimo.musicplayer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.bellissimo.musicplayer.presentation.music.MusicListScreen
import uz.bellissimo.musicplayer.presentation.permission.PermissionScreen
import uz.bellissimo.musicplayer.ui.theme.MusicPlayerTheme
import uz.bellissimo.musicplayer.navigation.NavigatorHandler
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigatorHandler: NavigatorHandler

    @SuppressLint("FlowOperatorInvokedInComposition", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                val screen = if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED
                ) MusicListScreen() else PermissionScreen()

                Navigator(screen = screen) { navigator ->
                    navigatorHandler.navigatorState
                        .onEach { it.invoke(navigator) }
                        .launchIn(lifecycleScope)
                    CurrentScreen()
                }
            }
        }
    }
}

