package com.tc.example.domain.usecase.get15thCharacterUseCase

import com.tc.example.domain.utils.CHARACTER_INDEX_15

/**
 * Use case implementation for finding the 15th character from a given text content.
 *
 * ## Responsibilities
 * - Accept pre-fetched website content as plain text.
 * - Returns the 15th character (index 14, zero-based indexing).
 * - Returns `null` if the text length is shorter than 15.
 *
 * ## Algorithm
 * 1. Check if the input text length is at least 15.
 * 2. If yes, return the character at index 14.
 * 3. Otherwise, return `null`.
 *
 * ## Design Notes
 * - This use case is **pure** — it performs no network or I/O operations.
 * - Keeps separation of concerns by relying on a higher layer (repository or ViewModel)
 *   to provide the text content.
 *
 * ## Time Complexity
 * - **O(1)** → Direct access by index.
 *
 * ## Space Complexity
 * - **O(1)** → Constant memory usage.
 */
class Get15thCharacterUseCaseImpl : Get15thCharacterUseCase {
    override suspend operator fun invoke(content: String): Char? {
        return content.getOrNull(CHARACTER_INDEX_15)
    }
}