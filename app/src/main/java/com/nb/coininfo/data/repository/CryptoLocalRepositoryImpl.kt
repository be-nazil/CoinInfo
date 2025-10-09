package com.nb.coininfo.data.repository

import com.nb.coininfo.data.local.CoinInfoDao
import com.nb.coininfo.data.models.CoinDetails
import com.nb.coininfo.data.models.CoinDetailsEntity
import com.nb.coininfo.data.models.CoinEntity
import com.nb.coininfo.data.models.MoverEntity
import com.nb.coininfo.data.models.TodayOHLCEntityItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CryptoLocalRepo {
    fun insertCoinList(coinList: List<CoinEntity>)
    suspend fun insertCoin(coinEntity: CoinEntity)
    fun insertTodayOHLCList(coinList: List<TodayOHLCEntityItem>)
    suspend fun insertTodayOHLC(coinOHLC: TodayOHLCEntityItem)
    suspend fun getTodayOHLC() : Flow<TodayOHLCEntityItem?>

    suspend fun getCoinsList() : Flow<List<CoinEntity>?>
    suspend fun getCoinTopCoins() : Flow<List<CoinEntity>?>
    suspend fun getCoin(id: String) : Flow<CoinEntity?>
    suspend fun insertTopMovers(movers: List<MoverEntity>)
    suspend fun getTopGainerList() : Flow<List<MoverEntity>?>
    suspend fun getTopLoserList() : Flow<List<MoverEntity>?>
    suspend fun insertCoinDetails(coinDetailsEntity: CoinDetails)

    fun getCoinDetails(coinId: String) : Flow<CoinDetailsEntity?>
}


class CryptoLocalRepositoryImpl @Inject constructor(private val coinInfoDao: CoinInfoDao): CryptoLocalRepo {

    override fun insertCoinList(coinList: List<CoinEntity>) {
        coinInfoDao.insertCoins(coinList)
    }

    override suspend fun insertCoin(coinEntity: CoinEntity) {
        coinInfoDao.insertCoin(coinEntity)
    }

    override fun insertTodayOHLCList(coinList: List<TodayOHLCEntityItem>) {
        coinInfoDao.insertTodayOHLCList(coinList)
    }

    override suspend fun insertTodayOHLC(coinOHLC: TodayOHLCEntityItem) {
        coinInfoDao.insertTodayOHLC(coinOHLC)
    }

    override suspend fun getTodayOHLC(): Flow<TodayOHLCEntityItem?> {
        return coinInfoDao.getTodayOHLC()
    }

    override suspend fun getCoinsList(): Flow<List<CoinEntity>?> {
        return coinInfoDao.getCoinsList()
    }

    override suspend fun getCoin(id: String): Flow<CoinEntity?> {
        return coinInfoDao.getCoin(id)
    }

    override suspend fun getCoinTopCoins(): Flow<List<CoinEntity>?> {
        return coinInfoDao.getCoinTopCoins()
    }

    override suspend fun insertTopMovers(movers: List<MoverEntity>) {
        return coinInfoDao.insertTopMovers(movers)
    }

    override suspend fun getTopGainerList(): Flow<List<MoverEntity>?> {
        return coinInfoDao.getTopGainerList()
    }

    override suspend fun getTopLoserList(): Flow<List<MoverEntity>?> {
        return coinInfoDao.getTopLoserList()
    }

    override suspend fun insertCoinDetails(coinDetailsEntity: CoinDetails) {
        coinInfoDao.insertCoinDetails(coinDetailsEntity.toCoinDetailsEntity())
    }

    override fun getCoinDetails(coinId: String): Flow<CoinDetailsEntity?> {
        return coinInfoDao.getCoinDetails(coinId)
    }
}