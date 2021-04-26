package com.benjaminearley.stockcost.repository.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.benjaminearley.stockcost.data.Product
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideNextDoorDb(@ApplicationContext context: Context): StockCostDatabase =
        Room
            .databaseBuilder(context, StockCostDatabase::class.java, DbName)
            .fallbackToDestructiveMigration()
            .build()

    private const val DbName = "stock-cost"
}

@Database(
    entities = [Product::class],
    version = 1
)
abstract class StockCostDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}