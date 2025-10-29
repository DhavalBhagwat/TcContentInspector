package com.tc.example.domain.usecase.getEvery15thCharacterUseCase

import com.tc.example.domain.utils.CHARACTER_INDEX_15
import com.tc.example.domain.utils.CHARACTER_INDEX_16

/**
 * Use case implementation for extracting every 15th character (15th, 30th, 45th, etc.)
 * from a given plain text content.
 *
 * ## Responsibilities
 * - Accept pre-fetched website content as plain text.
 * - Iterates through the text starting from the 15th character (index 14).
 * - Collects every subsequent 15th character into a list.
 * - Returns an empty list if the text length is shorter than 15.
 *
 * ## Algorithm
 * 1. Check if the input text length is at least 15.
 * 2. Start iteration from index 14 (the 15th character).
 * 3. Increment index by 15 each step, collecting the corresponding character.
 * 4. Return the accumulated list of characters.
 *
 * ## Design Notes
 * - This use case is **pure** and does not perform any I/O operations.
 * - Keeps domain logic independent of the data retrieval layer.
 * - Enables parallel execution alongside other text-processing use cases.
 *
 * ## Time Complexity
 * - **O(n)** → Linear traversal with constant step (≈ n/15 iterations).
 *
 * ## Space Complexity
 * - **O(n/15)** → Stores roughly one character for every 15 in the input text.
 */
class GetEvery15thCharacterUseCaseImpl : GetEvery15thCharacterUseCase {

    override suspend operator fun invoke(content: String): List<Char> {
        val result = mutableListOf<Char>()
        for (i in CHARACTER_INDEX_15 until content.length step CHARACTER_INDEX_16) {
            result.add(content[i])
        }
        return result
    }
}