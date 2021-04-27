package com.benjaminearley.stockcost.repository

sealed class StockCostError {
    object NotFound : StockCostError()
    object NetworkError : StockCostError()
    object DatabaseError : StockCostError()
}