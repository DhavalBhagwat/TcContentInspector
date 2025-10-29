package com.tc.example.domain.model

/**
 * UI state data class representing the state of the main screen.
 *
 * @param isLoading15th [Boolean] indicates if the 15th character is being loaded.
 * @param isLoadingEvery15th [Boolean] indicates if every 15th character is being loaded.
 * @param isLoadingWordCount [Boolean] indicates if the word count is being loaded.
 * @param fifteenthChar [Char] 15th character of the web content.
 * @param every15thChars [List] of [Char] list of every 15th character from the web content.
 * @param webContent [String] raw web content as a string.
 * @param wordCount [Map] of [String] and [Int] containing word counts from the web content.
 * @param error [String] error message, if any.
 */
data class UiState(
    val isLoading15th: Boolean = false,
    val isLoadingEvery15th: Boolean = false,
    val isLoadingWordCount: Boolean = false,
    val fifteenthChar: Char? = null,
    val every15thChars: List<Char> = emptyList(),
    val webContent: String? = null,
    val wordCount: Map<String, Int> = emptyMap(),
    val error: String? = null
)