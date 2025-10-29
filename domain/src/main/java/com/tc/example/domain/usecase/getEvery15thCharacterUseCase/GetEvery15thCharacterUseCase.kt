package com.tc.example.domain.usecase.getEvery15thCharacterUseCase

/**
 * Use case to get every 15th character from the given content.
 */
interface GetEvery15thCharacterUseCase {

    /**
     * Invokes the use case to get every 15th character from the given content.
     *
     * @param content [String] plain text content from website.
     * @return [List] of [Char] list of every 15th character of the content.
     */
    suspend operator fun invoke(content: String): List<Char>
}