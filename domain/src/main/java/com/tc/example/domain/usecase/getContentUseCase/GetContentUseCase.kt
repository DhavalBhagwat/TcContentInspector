package com.tc.example.domain.usecase.getContentUseCase

import com.tc.example.domain.model.ContentResponse

/**
 * Use case to fetch website content from a given URL.
 */
interface GetContentUseCase {

    /**
     * Invokes the use case to fetch website content from the given URL.
     *
     * @return [ContentResponse] containing plain text content of the website.
     */
    suspend operator fun invoke(): ContentResponse
}