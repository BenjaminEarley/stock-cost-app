package com.benjaminearley.stockcost.repository.network

import androidx.core.os.LocaleListCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClientModule {
    @Singleton
    @Provides
    fun provideClient(engine: OkHttpClient): HttpClient = HttpClient(OkHttp) {
        engine {
            preconfigured = engine
        }
        defaultRequest {
            header("Authorization", "Bearer $token")
            header("Accept-Language", acceptLanguageHeader)
        }
        install(JsonFeature) {
            val json = Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
            serializer = KotlinxSerializer(json)
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.tag("NetworkCall").i(message)
                }
            }
            level = LogLevel.ALL
        }
    }

    private val acceptLanguageHeader: String =
        LocaleListCompat
            .getAdjustedDefault()
            .let { list -> (0 until list.size()).map { list.get(it) } }
            .joinToString(",") { it.toLanguageTag() }

    private const val token =
        "eyJhbGciOiJIUzI1NiJ9.eyJyZWZyZXNoYWJsZSI6ZmFsc2UsInN1YiI6ImJiMGNkYTJiLWExMGUtNGVkMy1hZDV" +
                "hLTBmODJiNGMxNTJjNCIsImF1ZCI6ImJldGEuZ2V0YnV4LmNvbSIsInNjcCI6WyJhcHA6bG9naW4iLCJ" +
                "ydGY6bG9naW4iXSwiZXhwIjoxODIwODQ5Mjc5LCJpYXQiOjE1MDU0ODkyNzksImp0aSI6ImI3MzlmYjg" +
                "wLTM1NzUtNGIwMS04NzUxLTMzZDFhNGRjOGY5MiIsImNpZCI6Ijg0NzM2MjI5MzkifQ.M5oANIi2nBtS" +
                "fIfhyUMqJnex-JYg6Sm92KPYaUL9GKg"
}