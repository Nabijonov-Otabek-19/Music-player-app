package uz.gita.musicplayer_bek.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.musicplayer_bek.presentation.music.MusicListContract
import uz.gita.musicplayer_bek.presentation.music.MusicListDirectionImpl
import uz.gita.musicplayer_bek.presentation.permission.PermissionDirection
import uz.gita.musicplayer_bek.presentation.permission.PermissionDirectionImpl
import uz.gita.musicplayer_bek.presentation.play.PlayDirection
import uz.gita.musicplayer_bek.presentation.play.PlayDirectionImpl

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