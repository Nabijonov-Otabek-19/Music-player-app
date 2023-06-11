package uz.gita.musicplayer_bek.broadcast

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import uz.gita.musicplayer_bek.utils.base.checkCallPermissions

class CallBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_CALL -> {

            }
        }
    }
}