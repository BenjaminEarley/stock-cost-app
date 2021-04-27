package com.benjaminearley.stockcost.repository.network

import arrow.core.Either
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
    abstract fun bindSubscriptionService(
        subscriptionService: SubscriptionServiceImpl
    ): SubscriptionService
}

interface SubscriptionService {
    suspend fun getProductPrice(
        securityId: String,
        callback: (price: String) -> Unit
    ): Either<Throwable, Unit>
}

class SubscriptionServiceImpl @Inject constructor(
    private val client: HttpClient,
    private val json: Json
) : SubscriptionService {

    override suspend fun getProductPrice(
        securityId: String,
        callback: (price: String) -> Unit
    ): Either<Throwable, Unit> = Either.catch {
        client.wss(
            method = HttpMethod.Get,
            host = "rtf.beta.getbux.com",
            path = "/subscriptions/me"
        ) {

            val request = json.encodeToString(
                NetworkRequestSubscription(subscribeTo = listOf("trading.product.$securityId"))
            )

            Timber.d(request)
            send(request)

            val data = when (val status = incoming.consumeAsFlow().first()) {
                is Frame.Text -> json.decodeFromString<NetworkSubscription>(status.readText())
                else -> {
                    Timber.d("Wrong Frame Type")
                    null
                }
            }
            Timber.d(data.toString())
            if (data?.type != "connect.connected") return@wss
            Timber.d("Channel being consumed")
            incoming
                .consumeAsFlow()
                .mapNotNull {
                    when (it) {
                        is Frame.Text ->
                            json.decodeFromString<NetworkSubscription>(it.readText())
                        else -> {
                            Timber.d("Wrong Frame Type")
                            null
                        }
                    }
                }
                .filter { it.type == "trading.quote" }
                .collect { result ->
                    Timber.d(result.toString())
                    result.body?.currentPrice?.let { callback(it) }
                }
        }
    }
}