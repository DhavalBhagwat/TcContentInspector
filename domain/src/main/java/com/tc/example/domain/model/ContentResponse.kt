package com.tc.example.domain.model

/**
 * Data class representing the content fetched from a URL.
 *
 * @property content [String] plain text content of the website.
 */
data class ContentResponse(
    val content: String
)