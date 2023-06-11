package uz.gita.musicplayer_bek

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
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
import uz.gita.musicplayer_bek.broadcast.CallBroadcastReceiver
import uz.gita.musicplayer_bek.navigation.NavigatorHandler
import uz.gita.musicplayer_bek.presentation.music.MusicListScreen
import uz.gita.musicplayer_bek.presentation.permission.PermissionScreen
import uz.gita.musicplayer_bek.ui.theme.MusicPlayerTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigatorHandler: NavigatorHandler

    @Inject
    lateinit var receiver: CallBroadcastReceiver

    @SuppressLint("FlowOperatorInvokedInComposition", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                val screen = if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) ==
                    PackageManager.PERMISSION_GRANTED
                ) MusicListScreen() else PermissionScreen()

                Navigator(screen = screen) { navigator ->
                    navigatorHandler.navigatorState
                        .onEach { it.invoke(navigator) }
                        .launchIn(lifecycleScope)
                    CurrentScreen()
                }

                val intentFilter = IntentFilter().apply {
                    addAction(Intent.ACTION_CALL)
                    addAction(Intent.ACTION_NEW_OUTGOING_CALL)
                }

                this.registerReceiver(receiver, intentFilter)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}