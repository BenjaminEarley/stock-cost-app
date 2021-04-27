package com.benjaminearley.stockcost.repository.network

import arrow.core.Either
import com.benjaminearley.stockcost.BuildConfig
import com.benjaminearley.stockcost.repository.network.data.NetworkRequestSubscription
import com.benjaminearley.stockcost.repository.network.data.NetworkSubscription
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
abstract class SubscriptionServiceModule {
    @Binds
    abstract fun bindLivePriceService(
        livePriceService: LivePriceServiceImpl
    ): LivePriceService
}

interface LivePriceService {
    suspend fun getProductPrice(
        securityId: String,
        latestPrice: (price: String) -> Unit
    ): Either<Throwable, Unit>
}

class LivePriceServiceImpl @Inject constructor(
    private val client: HttpClient,
    private val json: Json
) : LivePriceService {

    override suspend fun getProductPrice(
        securityId: String,
        latestPrice: (price: String) -> Unit
    ): Either<Throwable, Unit> = Either.catch {
        client.wss(
            method = HttpMethod.Get,
            host = BuildConfig.BASE_SOCKET_URL,
            path = "/subscriptions/me"
        ) {

            val data = when (val status = incoming.receive()) {
                is Frame.Text -> json.decodeFromString<NetworkSubscription>(status.readText())
                else -> null
            }

            if (data?.type != "connect.connected") return@wss

            val request = json.encodeToString(
                NetworkRequestSubscription(subscribeTo = listOf("trading.product.$securityId"))
            )

            send(request)

            incoming
                .consumeAsFlow()
                .mapNotNull {
                    when (it) {
                        is Frame.Text ->
                            json.decodeFromString<NetworkSubscription>(it.readText())
                        else -> null
                    }
                }
                .filter { it.type == "trading.quote" }
                .collect { result ->
                    Timber.d(result.toString())
                    result.body?.currentPrice?.let { latestPrice(it) }
                }
        }
    }
}