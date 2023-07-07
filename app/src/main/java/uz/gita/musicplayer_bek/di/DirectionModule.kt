package uz.gita.musicplayer_bek.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.musicplayer_bek.presentation.music.MusicListContract
import uz.gita.musicplayer_bek.presentation.music.MusicListDirectionImpl
import uz.gita.musicplayer_bek.presentation.play.PlayContract
import uz.gita.musicplayer_bek.presentation.play.PlayDirection
import uz.gita.musicplayer_bek.presentation.playlist.PlayListContract
import uz.gita.musicplayer_bek.presentation.playlist.PlayListDirection

@Module
@InstallIn(ViewModelComponent::class)
interface DirectionModule {

    @Binds
    fun bindMusicListDirection(impl: MusicListDirectionImpl): MusicListContract.MusicListDirection

    @Binds
    fun bindPlayDirection(impl: PlayDirection): PlayContract.Direction

    @Binds
    fun bindPlayListDirection(imple: PlayListDirection): PlayListContract.Direction
}