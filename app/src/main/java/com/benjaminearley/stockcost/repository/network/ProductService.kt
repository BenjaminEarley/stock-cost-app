package com.benjaminearley.stockcost.repository.network

import com.benjaminearley.stockcost.BuildConfig
import com.benjaminearley.stockcost.repository.data.Product
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductServiceModule {
    @Binds
    abstract fun bindProductService(productService: ProductServiceImpl): ProductService
}

interface ProductService {
    suspend fun getProduct(productId: String): Product
}

@Singleton
class ProductServiceImpl @Inject constructor(private val client: HttpClient) : ProductService {
    override suspend fun getProduct(productId: String): Product = client
        .get("$baseApiUrl/core/23/products/$productId")

    companion object {
        const val baseApiUrl = BuildConfig.BASE_API_URL
    }
}