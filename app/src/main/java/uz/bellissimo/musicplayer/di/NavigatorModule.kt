package uz.bellissimo.musicplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.bellissimo.musicplayer.navigation.AppNavigator
import uz.bellissimo.musicplayer.navigation.NavigationDispatcher
import uz.bellissimo.musicplayer.navigation.NavigatorHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigatorModule {

    @[Binds Singleton]
    fun bindAppNavigator(impl : NavigationDispatcher)  : AppNavigator

    @[Binds Singleton]
    fun bindNavigatorHandler(impl : NavigationDispatcher)  : NavigatorHandler

}

