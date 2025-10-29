package com.tc.example.domain.usecase.getContentUseCase

import com.tc.example.domain.model.ContentResponse
import com.tc.example.domain.repository.ContentRepository
import com.tc.example.domain.usecase.getContentUseCase.GetContentUseCase

/**
 * Use case implementation to fetch website content from a given URL.
 *
 * @param contentRepository [ContentRepository] to fetch content from.
 */
class GetContentUseCaseImpl(
    private val contentRepository: ContentRepository
) : GetContentUseCase {
    override suspend operator fun invoke(): ContentResponse {
        return contentRepository.fetchContent()
    }
}