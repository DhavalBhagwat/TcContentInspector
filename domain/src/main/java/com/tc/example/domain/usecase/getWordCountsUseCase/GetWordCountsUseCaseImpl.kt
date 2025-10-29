package com.tc.example.domain.usecase.getWordCountsUseCase

/**
 * Use case implementation for counting **occurrences of each unique word** (case-insensitive)
 * in a given plain text content.
 *
 * ## Responsibilities
 * - Accept pre-fetched website content as plain text.
 * - Split the content into words using whitespace as a delimiter (spaces, tabs, newlines, etc.).
 * - Normalize words to lowercase to ensure case-insensitive counting.
 * - Skip any blank tokens caused by multiple spaces or formatting.
 * - Produce a frequency map where each key is a unique word and value is its occurrence count.
 *
 * ## Algorithm
 * 1. Split the input string using the regular expression `\\s+` to handle all whitespace.
 * 2. Convert each resulting token to lowercase.
 * 3. Filter out any empty strings.
 * 4. Use a mutable map to count occurrences (`wordCount[word] = wordCount.getOrDefault(word, 0) + 1`).
 * 5. Return the map of unique words and their counts.
 *
 * ## Example
 * ```
 * Input:
 * "<p> TC Hello World </p> TC"
 *
 * Output:
 * {
 *   "<p>" = 1,
 *   "tc" = 2,
 *   "hello" = 1,
 *   "world" = 1,
 *   "</p>" = 1
 * }
 * ```
 *
 * ## Design Notes
 * - This use case performs **pure computation** — no I/O operations.
 * - Decouples domain logic from data fetching, promoting testability.
 * - Designed for **parallel execution** with other use cases.
 *
 * ## Time Complexity
 * - **O(n)** → Linear traversal through all words in the text.
 *
 * ## Space Complexity
 * - **O(u)** → `u` represents the number of unique words in the text.
 */
class GetWordCountsUseCaseImpl : GetWordCountsUseCase {

    override suspend operator fun invoke(content: String): Map<String, Int> {
        return content
            .split("\\s+".toRegex())  // split by whitespace
            .filter { it.isNotBlank() }
            .groupingBy { it.lowercase() }
            .eachCount()
    }
}