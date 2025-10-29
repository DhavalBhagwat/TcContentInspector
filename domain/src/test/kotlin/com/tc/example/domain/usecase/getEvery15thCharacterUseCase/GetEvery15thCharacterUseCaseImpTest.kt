package com.tc.example.domain.usecase.getEvery15thCharacterUseCase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetEvery15thCharacterUseCaseImplTest {

    private lateinit var useCase: GetEvery15thCharacterUseCaseImpl

    @Before
    fun setup() {
        useCase = GetEvery15thCharacterUseCaseImpl()
    }

    @Test
    fun `returns empty list when content shorter than 15 characters`() = runTest {
        // Arrange
        val content = "short_text"

        // Act
        val result = useCase(content)

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `returns single character when content has exactly 15 characters`() = runTest {
        // Arrange
        val content = "abcdefghijklmno"

        // Act
        val result = useCase(content)

        // Assert
        assertEquals(listOf('o'), result)
    }

    @Test
    fun `returns correct characters for longer text`() = runTest {
        // Arrange
        val content = ('a'..'z').joinToString("")

        // Act
        val result = useCase(content)

        // Assert
        assertEquals(listOf('o'), result)
    }

    @Test
    fun `returns multiple characters for content with multiple 15-char intervals`() = runTest {
        // Arrange
        val content = (1..45).joinToString("") { it.toString().take(1) }

        // Act
        val result = useCase(content)

        // Assert
        assertEquals(3, result.size)
    }

    @Test
    fun `returns empty list for empty string`() = runTest {
        // Arrange
        val content = ""

        // Act
        val result = useCase(content)

        // Assert
        assertTrue(result.isEmpty())
    }
}