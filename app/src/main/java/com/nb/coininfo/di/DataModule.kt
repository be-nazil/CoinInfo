package com.nb.coininfo.di

import android.content.Context
import androidx.room.RoomDatabase
import com.nb.coininfo.data.local.CoinDataBase
import com.nb.coininfo.data.local.CoinInfoDao
import com.nb.coininfo.data.repository.CryptoLocalRepo
import com.nb.coininfo.data.repository.CryptoLocalRepository
import com.nb.coininfo.data.repository.CryptoLocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context) : CoinDataBase {
        return CoinDataBase.invoke(context)
    }

    @Provides
    @Singleton
    fun provideCoinInfoDao(dataBase: CoinDataBase): CoinInfoDao {
        return dataBase.coinInfoDAO()
    }

    @Provides
    @Singleton
    fun provideCryptoLocalRepository(coinInfoDao: CoinInfoDao): CryptoLocalRepo {
        return CryptoLocalRepositoryImpl(coinInfoDao)
    }

}