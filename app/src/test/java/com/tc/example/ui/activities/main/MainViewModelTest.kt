package com.tc.example.ui.activities.main

import com.tc.example.domain.model.ContentResponse
import com.tc.example.domain.model.UiState
import com.tc.example.domain.usecase.get15thCharacterUseCase.Get15thCharacterUseCase
import com.tc.example.domain.usecase.getContentUseCase.GetContentUseCase
import com.tc.example.domain.usecase.getEvery15thCharacterUseCase.GetEvery15thCharacterUseCase
import com.tc.example.domain.usecase.getWordCountsUseCase.GetWordCountsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val getContentUseCase = mockk<GetContentUseCase>()
    private val get15thCharacterUseCase = mockk<Get15thCharacterUseCase>()
    private val getEvery15thCharacterUseCase = mockk<GetEvery15thCharacterUseCase>()
    private val getWordCountUseCase = mockk<GetWordCountsUseCase>()

    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(
            getContentUseCase,
            get15thCharacterUseCase,
            getEvery15thCharacterUseCase,
            getWordCountUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadContent updates UI state when all use cases succeed`() = runTest {
        // Arrange
        val content = "This is a test string for TC"
        coEvery { getContentUseCase() } returns ContentResponse(content)
        coEvery { get15thCharacterUseCase(content) } returns 'a'
        coEvery { getEvery15thCharacterUseCase(content) } returns listOf('a', 'b', 'c')
        coEvery { getWordCountUseCase(content) } returns mapOf("this" to 1, "is" to 1)

        // Act
        viewModel.loadContent()
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading15th)
        assertEquals(false, state.isLoadingEvery15th)
        assertEquals(false, state.isLoadingWordCount)
        assertEquals('a', state.fifteenthChar)
        assertEquals(listOf('a', 'b', 'c'), state.every15thChars)
        assertEquals(mapOf("this" to 1, "is" to 1), state.wordCount)
        assertNull(state.error)
    }

    @Test
    fun `loadContent sets error when content fetch fails`() = runTest {
        // Arrange
        coEvery { getContentUseCase() } throws Exception("Network error")

        // Act
        viewModel.loadContent()
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals("Network error", state.error)
        assertEquals(false, state.isLoading15th)
        assertEquals(false, state.isLoadingEvery15th)
        assertEquals(false, state.isLoadingWordCount)
    }

    @Test
    fun `loadContent handles partial failure gracefully`() = runTest {
        // Arrange
        val content = "Hello TC"
        coEvery { getContentUseCase() } returns ContentResponse(content)
        coEvery { get15thCharacterUseCase(content) } throws Exception("char error")
        coEvery { getEvery15thCharacterUseCase(content) } returns listOf('x', 'y')
        coEvery { getWordCountUseCase(content) } returns mapOf("hello" to 1)

        // Act
        viewModel.loadContent()
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(listOf('x', 'y'), state.every15thChars)
        assertEquals(mapOf("hello" to 1), state.wordCount)
        assertEquals("char error", state.error)
    }

    @Test
    fun `clearError resets error to null`() = runTest {
        // Arrange
        val initialState = UiState(error = "Some error")
        viewModel = spyk(viewModel)

        // Act
        viewModel.clearError()

        // Assert
        val state = viewModel.uiState.value
        assertNull(state.error)
    }

    @Test
    fun `loadContent runs tasks concurrently and updates results independently`() = runTest {
        // Arrange
        val content = "This is a concurrent test for TC"
        coEvery { getContentUseCase() } returns ContentResponse(content)

        var fifteenthExecuted = false
        var every15thExecuted = false
        var wordCountExecuted = false

        coEvery { get15thCharacterUseCase(content) } coAnswers {
            delay(300)
            fifteenthExecuted = true
            'X'
        }

        coEvery { getEvery15thCharacterUseCase(content) } coAnswers {
            delay(100)
            every15thExecuted = true
            listOf('E', 'V')
        }

        coEvery { getWordCountUseCase(content) } coAnswers {
            delay(200)
            wordCountExecuted = true
            mapOf("this" to 1, "test" to 1)
        }

        // Act
        viewModel.loadContent()

        // Move virtual time forward gradually
        advanceTimeBy(120) // every15th should complete first
        assertEquals(true, every15thExecuted)
        assertEquals(false, fifteenthExecuted)
        assertEquals(false, wordCountExecuted)

        advanceTimeBy(100) // word count completes second
        assertEquals(true, wordCountExecuted)
        assertEquals(false, fifteenthExecuted)

        advanceTimeBy(1000) // fifteenth char completes last
        assertEquals(true, fifteenthExecuted)

        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals('X', state.fifteenthChar)
        assertEquals(listOf('E', 'V'), state.every15thChars)
        assertEquals(mapOf("this" to 1, "test" to 1), state.wordCount)
        assertNull(state.error)
    }
}