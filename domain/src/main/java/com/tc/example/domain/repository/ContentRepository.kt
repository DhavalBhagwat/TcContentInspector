package com.tc.example.domain.repository

import com.tc.example.domain.model.ContentResponse

/**
 * Repository for fetching and caching website content.
 */
interface ContentRepository {
    /**
     * Function to fetch the plain text content from a given URL.
     *
     * @return [ContentResponse] The plain text content of the website.
     */
    suspend fun fetchContent(): ContentResponse
}