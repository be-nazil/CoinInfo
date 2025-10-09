package com.nb.coininfo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.nb.coininfo.data.models.CoinDetailsEntity
import com.nb.coininfo.data.models.CoinEntity
import com.nb.coininfo.data.models.MoverEntity
import com.nb.coininfo.data.models.TodayOHLCEntityItem
import com.nb.coininfo.data.models.TopMoversEntity

@Database(
    entities = [
        CoinEntity::class,
        MoverEntity::class,
        CoinDetailsEntity::class,
        TodayOHLCEntityItem::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MoshiTypeConverters::class)
abstract class CoinDataBase: RoomDatabase() {
    companion object {
        private const val DB_NAME = "coin_data.db"

        @Volatile
        private var INSTANCE : CoinDataBase? = null
        val lock = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(lock) {
            INSTANCE ?:buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context): CoinDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                CoinDataBase::class.java,
                DB_NAME
            ).build()
        }

    }

    abstract fun coinInfoDAO(): CoinInfoDao

}