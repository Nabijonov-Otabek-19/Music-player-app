package uz.bellissimo.musicplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.bellissimo.musicplayer.presentation.music.MusicListContract
import uz.bellissimo.musicplayer.presentation.music.MusicListDirectionImpl
import uz.bellissimo.musicplayer.presentation.permission.PermissionDirection
import uz.bellissimo.musicplayer.presentation.permission.PermissionDirectionImpl
import uz.bellissimo.musicplayer.presentation.play.PlayDirection
import uz.bellissimo.musicplayer.presentation.play.PlayDirectionImpl

@Module
@InstallIn(ViewModelComponent::class)
interface DirectionModule {

    @Binds
    fun bindPermissionDirection(impl: PermissionDirectionImpl) : PermissionDirection

    @Binds
    fun bindMusicListDirection(impl : MusicListDirectionImpl) : MusicListContract.MusicListDirection

    @Binds
    fun bindPlayDirection(impl: PlayDirectionImpl) : PlayDirection
}