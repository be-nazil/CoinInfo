package com.nb.coininfo.data.repository

import com.nb.coininfo.data.apiservices.CoinService2
import com.nb.coininfo.data.apiservices.CoinsService
import com.nb.coininfo.data.models.CoinDetails
import com.nb.coininfo.data.models.CoinDetailsEntity
import com.nb.coininfo.data.models.CoinEntity
import com.nb.coininfo.data.models.CoinGraphData
import com.nb.coininfo.data.models.GlobalStatsEntity
import com.nb.coininfo.data.models.MarketChartResponse
import com.nb.coininfo.data.models.TodayOHLCEntityItem
import com.nb.coininfo.data.models.TopMoversEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.http.Query
import javax.inject.Inject

class CryptoRepository @Inject constructor(
    private val coinsService: CoinsService,
    private val coinsService2: CoinService2,
    private val cryptoLocalRepository: CryptoLocalRepository
){
    suspend fun getCoin(id: String): Flow<CoinDetails?> {
        return flow {
            emit(coinsService.getCoin(id))
        }
    }

    suspend fun getGraphData(id: String): Flow<List<CoinGraphData?>?> {
        return flow {
            emit(coinsService.getGraphData(id))
        }
    }

    fun getChartData(id: String): Flow<MarketChartResponse> {
        return flow {
            emit(coinsService2.getMarketChart(id.substringAfter("-", id.takeLast(id.length-4))))
        }
    }

    suspend fun getGlobalStats(): Flow<GlobalStatsEntity?> {
        return flow {
            emit(coinsService.getGlobalStats())
        }

    }
    suspend fun getCoins(): Flow<List<CoinEntity?>> {
        return flow {
            emit(coinsService.getCoins())
        }
    }


    suspend fun getTodayOHLC(): Flow<List<TodayOHLCEntityItem?>?> {
        return flow {
            emit(coinsService.getTodayOHLC())
        }
    }

    suspend fun getTop10Movers(): Flow<TopMoversEntity?> {
        return flow {
            emit(coinsService.getTop10Movers(""))
        }
    }
    suspend fun getMovers(): Flow<TopMoversEntity?> {
        return flow {
            emit(coinsService.getMovers())
        }
    }
}