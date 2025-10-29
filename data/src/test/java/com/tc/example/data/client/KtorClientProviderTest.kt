package com.tc.example.data.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttpEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.pluginOrNull
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KtorClientProviderTest {

    private lateinit var client: HttpClient

    @Before
    fun setup() {
        client = KtorClientProvider.create()
    }

    @After
    fun tearDown() {
        client.close()
    }

    @Test
    fun `create returns HttpClient with OkHttp engine`() {
        // Assert
        assertTrue(
            "Expected OkHttp engine but got ${client.engine::class.simpleName}",
            client.engine is OkHttpEngine,
        )
    }

    @Test
    fun `client installs Logging plugin with INFO level`() {
        // Arrange
        val loggingPlugin = client.pluginOrNull(Logging)

        // Act
        assertNotNull("Logging plugin should be installed", loggingPlugin)

        // Assert
        assertEquals(
            "Logging level should be INFO",
            LogLevel.INFO,
            loggingPlugin?.level,
        )
    }

    @Test
    fun `client installs ContentNegotiation plugin with Json settings`() {
        // Arrange
        val plugin = client.pluginOrNull(ContentNegotiation)

        // Then
        assertNotNull("ContentNegotiation plugin should be installed", plugin)

        val expectedJson = Json {
            ignoreUnknownKeys = true
            prettyPrint = false
            isLenient = true
        }

        // Assert
        assertEquals(
            true,
            expectedJson.configuration.ignoreUnknownKeys,
        )
        assertEquals(
            "isLenient should be true",
            true,
            expectedJson.configuration.isLenient,
        )
    }
}