package uz.gita.musicplayer_bek.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.musicplayer_bek.R
import uz.gita.musicplayer_bek.data.model.CommandEnum
import uz.gita.musicplayer_bek.data.model.MusicData
import uz.gita.musicplayer_bek.utils.MyEventBus
import uz.gita.musicplayer_bek.utils.base.getMusicDataByPosition

class MusicService : Service() {

    companion object {
        const val CHANNEL_ID = "My music player"
        const val CHANNEL_NAME = "Music player"
    }

    private var _musicPlayer: MediaPlayer? = null
    private val musicPlayer get() = _musicPlayer!!

    override fun onBind(intent: Intent?): IBinder? = null
    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun createNotification(musicData: MusicData) {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCustomContentView(createRemoteView(musicData))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
        startForeground(1, notificationBuilder.build())
    }

    private fun createRemoteView(musicData: MusicData): RemoteViews {
        val view = RemoteViews(this.packageName, R.layout.remote_view)
        view.setTextViewText(R.id.textMusicName, musicData.title)
        view.setTextViewText(R.id.textArtistName, musicData.artist)

        if (_musicPlayer != null && !musicPlayer.isPlaying) {
            view.setImageViewResource(R.id.buttonManage, R.drawable.ic_pause)
        } else {
            view.setImageViewResource(R.id.buttonManage, R.drawable.ic_play)
        }

        view.setOnClickPendingIntent(R.id.buttonPrev, createPendingIntent(CommandEnum.PREV))
        view.setOnClickPendingIntent(R.id.buttonManage, createPendingIntent(CommandEnum.MANAGE))
        view.setOnClickPendingIntent(R.id.buttonNext, createPendingIntent(CommandEnum.NEXT))
        view.setOnClickPendingIntent(R.id.buttonCancel, createPendingIntent(CommandEnum.CLOSE))
        return view
    }

    private fun createPendingIntent(commandEnum: CommandEnum): PendingIntent {
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra("COMMAND", commandEnum)
        return PendingIntent.getService(
            this,
            commandEnum.amount,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (MyEventBus.cursor == null || MyEventBus.selectMusicPos == -1) return START_NOT_STICKY
        val command = intent?.extras?.getSerializable("COMMAND") as CommandEnum
        doneCommand(command)
        if (command.name != CommandEnum.CLOSE.name) {
            createNotification(MyEventBus.cursor!!.getMusicDataByPosition(MyEventBus.selectMusicPos))
        }
        return START_NOT_STICKY
    }

    private fun doneCommand(commandEnum: CommandEnum) {
        when (commandEnum) {

            CommandEnum.MANAGE -> {
                if (musicPlayer.isPlaying) doneCommand(CommandEnum.PAUSE)
                else doneCommand(CommandEnum.PLAY)
            }

            CommandEnum.PREV -> {
                if (MyEventBus.selectMusicPos - 1 == -1) {
                    MyEventBus.selectMusicPos = MyEventBus.cursor!!.count - 1
                } else {
                    --MyEventBus.selectMusicPos
                }
                doneCommand(CommandEnum.PLAY)
            }

            CommandEnum.NEXT -> {
                if (MyEventBus.selectMusicPos + 1 == MyEventBus.cursor!!.count) {
                    MyEventBus.selectMusicPos = 0
                } else {
                    ++MyEventBus.selectMusicPos
                }
                doneCommand(CommandEnum.PLAY)
            }

            CommandEnum.PLAY -> {
                val data = MyEventBus.cursor!!.getMusicDataByPosition(MyEventBus.selectMusicPos)
                scope.launch { MyEventBus.currentMusicData.emit(data) }

                MyEventBus.totalTime = data.duration.toInt()
                _musicPlayer?.pause()
                _musicPlayer = MediaPlayer.create(this, Uri.parse(data.data))
                musicPlayer.seekTo(MyEventBus.currentTime)
                musicPlayer.setOnCompletionListener {
                    doneCommand(CommandEnum.NEXT)
                }

                job?.cancel()
                job = moveProgress().onEach {
                    MyEventBus.currentTimeFlow.emit(it)
                }.launchIn(scope)
                scope.launch {
                    MyEventBus.isPlaying.emit(true)
                }
                musicPlayer.start()
            }

            CommandEnum.PAUSE -> {
                musicPlayer.pause()
                scope.launch {
                    MyEventBus.isPlaying.emit(false)
                }
                job?.cancel()
            }

            CommandEnum.CLOSE -> {
                musicPlayer.pause()
                ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
            }
        }
    }

    private fun moveProgress(): Flow<Int> = flow {
        for (i in MyEventBus.currentTime until MyEventBus.totalTime step 1000) {
            emit(i)
            delay(1000)
        }
    }
}