package com.nb.coininfo.data.repository

import androidx.compose.ui.util.fastFilterNotNull
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.nb.coininfo.data.models.CoinDetails
import com.nb.coininfo.data.models.CoinDetailsEntity
import com.nb.coininfo.data.models.CoinEntity
import com.nb.coininfo.data.models.MoverEntity
import com.nb.coininfo.data.models.TodayOHLCEntityItem
import com.nb.coininfo.data.models.TodayOHLCList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CryptoLocalRepository @Inject constructor(private val cryptoLocalRepo: CryptoLocalRepositoryImpl) {

    suspend fun insertCoin(coin: CoinEntity) {
        cryptoLocalRepo.insertCoin(coin)
    }

    suspend fun insertCoins(coinList: List<CoinEntity?>) {
        cryptoLocalRepo.insertCoinList(coinList.fastFilterNotNull())
    }

    suspend fun getCoin(id: String) : Flow<CoinEntity?> {
        return cryptoLocalRepo.getCoin(id)
    }
    suspend fun getCoinTopCoins() : Flow<List<CoinEntity>?> {
        return cryptoLocalRepo.getCoinTopCoins()
    }

    suspend fun insertOHLC(todayOHLC: TodayOHLCEntityItem) {
        cryptoLocalRepo.insertTodayOHLC(todayOHLC)
    }

    suspend fun insertOHLCList(todayOHLC: List<TodayOHLCEntityItem>) {
        cryptoLocalRepo.insertTodayOHLCList(todayOHLC)
    }

    suspend fun getTodayOHCLItem(): Flow<TodayOHLCEntityItem?> {
        return cryptoLocalRepo.getTodayOHLC()
    }

    suspend fun insertTopMovers(movers: List<MoverEntity>) {
        return cryptoLocalRepo.insertTopMovers(movers)
    }

    suspend fun getTopGainerList(): Flow<List<MoverEntity>?> {
        return cryptoLocalRepo.getTopGainerList()
    }
    suspend fun getTopLoserList(): Flow<List<MoverEntity>?> {
        return cryptoLocalRepo.getTopLoserList()
    }

    suspend fun insertCoinDetails(coinDetailsEntity: CoinDetails) {
        return cryptoLocalRepo.insertCoinDetails(coinDetailsEntity)
    }

    fun getCoinDetails(coinId: String): Flow<CoinDetailsEntity?> {
        return cryptoLocalRepo.getCoinDetails(coinId)
    }

    fun searchCoin(searchQuery: String): Flow<PagingData<CoinEntity>> {
        val dbQuery = "%${searchQuery.replace(' ', '%')}%"

        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                cryptoLocalRepo.searchCoin(dbQuery)
            }
        ).flow
    }

}