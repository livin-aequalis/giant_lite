package com.example.demoewallet.app.di

import com.example.demoewallet.app.network.CoingeckoApi
import com.example.demoewallet.app.network.CoingeckoApiImpl
import com.example.demoewallet.app.network.HttpExceptionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class CommonModule {

    @Provides
    @Singleton
    fun provideHttpExceptionHandler() = HttpExceptionHandler()

    @Provides
    @Singleton
    fun providelCoingeckoApiImpl(
        httpExceptionHandler: HttpExceptionHandler,
        coingeckoApi: CoingeckoApi
    ) = CoingeckoApiImpl(httpExceptionHandler, coingeckoApi)


}
