package com.benjaminearley.stockcost.repository

import com.benjaminearley.stockcost.repository.data.Product
import kotlinx.coroutines.flow.Flow


interface ProductsRepository {
    fun getProduct(productId: String): Flow<Product>
    fun getProducts(): Flow<List<Product>>
    suspend fun addProduct(product: Product)
    suspend fun addProducts(products: List<Product>)
}