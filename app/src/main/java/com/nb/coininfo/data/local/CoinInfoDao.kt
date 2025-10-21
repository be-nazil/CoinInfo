package com.nb.coininfo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nb.coininfo.data.models.CoinDetailsEntity
import com.nb.coininfo.data.models.CoinEntity
import com.nb.coininfo.data.models.MoverEntity
import com.nb.coininfo.data.models.TodayOHLCEntityItem
import com.nb.coininfo.data.models.TopMoversEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoins(coinList: List<CoinEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoin(coin: CoinEntity): Long

    @Query("SELECT * FROM coin_table")
    fun getCoinsList(): Flow<List<CoinEntity>?>

    @Query("SELECT * FROM coin_table WHERE rank != 0 ORDER BY rank ASC LIMIT 4")
    fun getCoinTopCoins(): Flow<List<CoinEntity>?>

    @Query("SELECT * FROM coin_table WHERE id = :id ")
    fun getCoin(id: String): Flow<CoinEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodayOHLCList(coinList: List<TodayOHLCEntityItem>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodayOHLC(coin: TodayOHLCEntityItem): Long

    @Query("SELECT * FROM today_ohlc ORDER BY id DESC LIMIT 1")
    fun getTodayOHLC(): Flow<TodayOHLCEntityItem?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopMovers(moversEntity: List<MoverEntity>)

    @Query("SELECT * FROM movers_table WHERE percent_change > 0 ORDER BY percent_change DESC")
    fun getTopGainerList(): Flow<List<MoverEntity>?>

    @Query("SELECT * FROM movers_table WHERE percent_change < 0 ORDER BY percent_change DESC")
    fun getTopLoserList(): Flow<List<MoverEntity>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinDetails(coin: CoinDetailsEntity): Long

    @Query("SELECT * FROM coin_details_table WHERE id = :coinId")
    fun getCoinDetails(coinId: String): Flow<CoinDetailsEntity?>

    @Query("SELECT * FROM coin_table WHERE name LIKE :request ORDER BY name ASC ")
    fun searchCoin(request: String): Flow<List<CoinEntity?>?>

}