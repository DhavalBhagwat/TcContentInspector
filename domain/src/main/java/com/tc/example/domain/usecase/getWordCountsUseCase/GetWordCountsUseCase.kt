package com.tc.example.domain.usecase.getWordCountsUseCase

/**
 * Use case to get word counts from the given content.
 */
interface GetWordCountsUseCase {

    /**
     * Invokes the use case to get word counts from the given content.
     *
     * @param content [String] plain text content from website.
     * @return [Map] of [String] to [Int] representing word counts.
     */
    suspend operator fun invoke(content: String): Map<String, Int>
}