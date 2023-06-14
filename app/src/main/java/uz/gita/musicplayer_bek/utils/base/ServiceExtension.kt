package uz.gita.musicplayer_bek.utils.base

import android.content.Context
import android.content.Intent
import android.os.Build
import uz.gita.musicplayer_bek.data.model.CommandEnum
import uz.gita.musicplayer_bek.service.MusicService

fun startMusicService(context: Context, commandEnum: CommandEnum) {
    val intent = Intent(context, MusicService::class.java)
    intent.putExtra("COMMAND", commandEnum)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else context.startService(intent)
}