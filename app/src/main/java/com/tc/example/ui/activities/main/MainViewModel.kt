package com.tc.example.ui.activities.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tc.example.utils.getStartTime
import com.tc.example.utils.timeDiff
import com.tc.example.domain.model.UiState
import com.tc.example.domain.usecase.get15thCharacterUseCase.Get15thCharacterUseCase
import com.tc.example.domain.usecase.getContentUseCase.GetContentUseCase
import com.tc.example.domain.usecase.getEvery15thCharacterUseCase.GetEvery15thCharacterUseCase
import com.tc.example.domain.usecase.getWordCountsUseCase.GetWordCountsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * [ViewModel] coordinating all three use cases.
 *
 * @property getContentUseCase [GetContentUseCase] use case to fetch website content.
 * @property get15thCharacterUseCase [Get15thCharacterUseCase] use case to get the 15th character.
 * @property getEvery15thCharacterUseCase [GetEvery15thCharacterUseCase] use case to get every 15th character.
 * @property getWordCountUseCase [GetWordCountsUseCase] use case to get word counts.
 */
class MainViewModel(
    private val getContentUseCase: GetContentUseCase,
    private val get15thCharacterUseCase: Get15thCharacterUseCase,
    private val getEvery15thCharacterUseCase: GetEvery15thCharacterUseCase,
    private val getWordCountUseCase: GetWordCountsUseCase
) : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    /** [MutableStateFlow] for UI state */
    private val _uiState = MutableStateFlow(UiState())

    /** UI state exposed as [StateFlow] */
    val uiState = _uiState.asStateFlow()

    /**
     * Function to fetch website the content from the given URL and launches
     * three concurrent coroutines to perform text analysis:
     * 1. Extract the 15th character.
     * 2. Extract every 15th character.
     * 3. Count word occurrences.
     */
    fun loadContent() {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading15th = true,
                        isLoadingEvery15th = true,
                        isLoadingWordCount = true,
                        fifteenthChar = null,
                        every15thChars = emptyList(),
                        wordCount = emptyMap(),
                        webContent = null,
                        error = null
                    )
                }
                Timber.tag(TAG).d("Network ::: Fetching website content started")
                val content = withContext(Dispatchers.IO) { getContentUseCase() }
                Timber.tag(TAG)
                    .d("Network ::: Website content fetched (${content.content.length} chars)")
                val text = content.content
                _uiState.update {
                    it.copy(webContent = text)
                }

                // Launch three concurrent text analysis coroutines
                launchAnalysisTasks(text)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    /**
     * Function to launch the three independent processing tasks concurrently.
     * Each task updates the UI state upon completion or failure.
     *
     * @param text [String] The website content to analyze.
     */
    private fun launchAnalysisTasks(text: String) {
        // 15th Character
        viewModelScope.launch(Dispatchers.Default) {
            val startTime = getStartTime()
            Timber.tag(TAG).d("15thChar ::: Processing started")
            runCatching { get15thCharacterUseCase(text) }
                .onSuccess { char ->
                    Timber.tag(TAG)
                        .d("15thChar ::: Completed in ${timeDiff(startTime)} ms ::: '$char'")
                    _uiState.update {
                        it.copy(fifteenthChar = char, isLoading15th = false)
                    }
                }
                .onFailure { handlePartialError(it, "15th Character") }
        }

        // Every 15th Character
        viewModelScope.launch(Dispatchers.Default) {
            val startTime = getStartTime()
            Timber.tag(TAG).d("Every15thChar ::: Processing started")
            runCatching { getEvery15thCharacterUseCase(text) }
                .onSuccess { chars ->
                    Timber.tag(TAG)
                        .d("Every15thChar ::: Completed in ${timeDiff(startTime)} ms ::: Count = ${chars.size}")
                    _uiState.update {
                        it.copy(every15thChars = chars, isLoadingEvery15th = false)
                    }
                }
                .onFailure { handlePartialError(it, "Every 15th Character") }
        }

        // Unique Word Count
        viewModelScope.launch(Dispatchers.Default) {
            val startTime = getStartTime()
            Timber.tag(TAG).d("WordCount ::: Processing started")
            runCatching { getWordCountUseCase(text) }
                .onSuccess { counts ->
                    Timber.tag(TAG)
                        .d("WordCount ::: Completed in ${timeDiff(startTime)} ms ::: Unique = ${counts.size}")
                    _uiState.update {
                        it.copy(wordCount = counts, isLoadingWordCount = false)
                    }
                }
                .onFailure { handlePartialError(it, "Word Count") }
        }
    }

    /**
     * Function to handle partial errors in individual processing tasks.
     * Updates the UI state with the error message for the specific task.
     *
     * @param e [Throwable] The exception that occurred.
     * @param context [String] The context of the error (e.g., which task).
     */
    private fun handlePartialError(e: Throwable, context: String) {
        Timber.tag(TAG).e(e, "Error in $context")
        _uiState.update {
            it.copy(error = "Error in $context: ${e.message}")
        }
    }

    /**
     * Function to handle fatal errors that occur during content fetching or processing.
     * Updates the UI state to reflect that loading has stopped and sets the error message.
     *
     * @param e [Throwable] The exception that occurred.
     */
    private fun handleError(e: Throwable) {
        Timber.tag(TAG).e(e, "Fatal error while processing content")
        _uiState.update {
            it.copy(
                isLoading15th = false,
                isLoadingEvery15th = false,
                isLoadingWordCount = false,
                error = e.message ?: "Unknown error"
            )
        }
    }

    /**
     * Function to clear the current error message in the UI state.
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
