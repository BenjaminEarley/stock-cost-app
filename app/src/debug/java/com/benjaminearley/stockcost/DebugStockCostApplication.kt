package com.benjaminearley.stockcost

import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DebugStockCostApplication : StockCostApplication() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}