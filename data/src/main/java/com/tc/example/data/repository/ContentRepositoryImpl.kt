package com.tc.example.data.repository

import com.tc.example.data.remote.ApiConstants
import com.tc.example.domain.model.ContentResponse
import com.tc.example.domain.repository.ContentRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Implementation of [ContentRepository] using Ktor HTTP client.
 *
 * @param client [HttpClient] by Ktor for making network requests.
 */
class ContentRepositoryImpl(
    private val client: HttpClient
) : ContentRepository {

    override suspend fun fetchContent(): ContentResponse =
        withContext(Dispatchers.IO) {
            try {
                val response = client.get(ApiConstants.TEST_URL)
                val content = response.bodyAsText()
                ContentResponse(content)
            } catch (e: Exception) {
                Timber.e(e, "Failed to fetch content from ${ApiConstants.TEST_URL}")
                ContentResponse("")
            }
        }
}