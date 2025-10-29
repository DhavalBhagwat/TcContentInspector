package com.tc.example.data.repository

import com.tc.example.domain.model.ContentResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import org.junit.Test
import timber.log.Timber
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before

class ContentRepositoryImplTest {

    @Before
    fun setup() {
        mockkStatic(Timber::class)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `fetchContent returns content when request succeeds`() = runTest {
        // Arrange
        val expectedText = "<html><body>Hello TC!</body></html>"
        val mockEngine = MockEngine { _ ->
            respond(
                content = expectedText,
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type" to listOf(ContentType.Text.Html.toString()))
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json() }
        }

        val repository = ContentRepositoryImpl(client)

        // Act
        val result = repository.fetchContent()

        // Assert
        assertEquals(ContentResponse(expectedText), result)
    }

    @Test
    fun `fetchContent returns error when request fails`() = runTest {
        // Arrange
        val mockEngine = MockEngine { _ ->
            respondError(HttpStatusCode.InternalServerError)
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json() }
        }

        val repository = ContentRepositoryImpl(client)

        // Act
        val result = repository.fetchContent()

        // Assert
        assertEquals(ContentResponse("Internal Server Error"), result)
    }


    @Test
    fun `fetchContent handles exception gracefully`() = runTest {
        // Arrange
        val mockEngine = MockEngine { throw RuntimeException("Network error") }
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json() }
        }
        val repository = ContentRepositoryImpl(client)

        // Act
        val result = repository.fetchContent()

        // Assert
        assertEquals(ContentResponse(""), result)
    }
}