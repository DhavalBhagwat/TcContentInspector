package com.tc.example.domain.usecase.get15thCharacterUseCase

/**
 * Use case to get the 15th character from the given content.
 */
interface Get15thCharacterUseCase {

    /**
     * Invokes the use case to get the 15th character from the given content.
     *
     * @param content [String] plain text content from website.
     * @return [Char] The 15th character of the content, or null if it doesn't exist.
     */
    suspend operator fun invoke(content: String): Char?
}