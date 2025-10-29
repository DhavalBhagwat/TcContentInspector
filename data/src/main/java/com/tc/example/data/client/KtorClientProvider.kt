package com.tc.example.data.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Class responsible for providing a configured Ktor HttpClient instance.
 */
object KtorClientProvider {

    /**
     * Function to create and configure an HttpClient with OkHttp engine,
     * logging, and JSON content negotiation.
     *
     * @return [HttpClient] configured instance.
     */
    fun create(): HttpClient {
        return HttpClient(OkHttp) {
            install(Logging) {
                level = LogLevel.INFO
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = false
                        isLenient = true
                    }
                )
            }
        }
    }
}