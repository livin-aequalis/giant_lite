package com.example.demoewallet.app.di

import com.example.demoewallet.app.network.CoingeckoApi
import com.example.demoewallet.app.network.NetworkApiCreator
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AccountFeatureModule {

    @Provides
    fun provideCoingeckoApi(networkApiCreator: NetworkApiCreator): CoingeckoApi {
        return networkApiCreator.create(CoingeckoApi::class.java)
    }
}
