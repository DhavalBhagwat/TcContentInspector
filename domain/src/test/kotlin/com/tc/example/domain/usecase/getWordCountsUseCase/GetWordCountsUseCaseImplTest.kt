package com.tc.example.domain.usecase.getWordCountsUseCase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetWordCountsUseCaseImplTest {

    private lateinit var useCase: GetWordCountsUseCaseImpl

    @Before
    fun setup() {
        useCase = GetWordCountsUseCaseImpl()
    }

    @Test
    fun `returns empty map for empty content`() = runTest {
        // Arrange
        val content = ""

        // Act
        val result = useCase(content)

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `returns correct count for single word`() = runTest {
        // Arrange
        val content = "Hello"

        // Act
        val result = useCase(content)

        // Assert
        assertEquals(mapOf("hello" to 1), result)
    }

    @Test
    fun `returns correct counts for repeated words ignoring case`() = runTest {
        // Arrange
        val content = "Hello hello HELLO world WORLD"

        // Act
        val result = useCase(content)

        // Assert
        assertEquals(mapOf("hello" to 3, "world" to 2), result)
    }

    @Test
    fun `splits words correctly on multiple spaces and newlines`() = runTest {
        // Arrange
        val content = "Hello   world\nthis   is\ta test"

        // Act
        val result = useCase(content)

        // Assert
        val expected = mapOf(
            "hello" to 1,
            "world" to 1,
            "this" to 1,
            "is" to 1,
            "a" to 1,
            "test" to 1
        )
        assertEquals(expected, result)
    }

    @Test
    fun `counts html-like tokens as normal words`() = runTest {
        // Arrange
        val content = "<p> Hello </p> Hello"

        // Act
        val result = useCase(content)

        // Assert
        val expected = mapOf(
            "<p>" to 1,
            "hello" to 2,
            "</p>" to 1
        )
        assertEquals(expected, result)
    }

    @Test
    fun `ignores extra blank entries when content has extra spaces`() = runTest {
        // Arrange
        val content = "   Hello     World   "

        // Act
        val result = useCase(content)

        // Assert
        val expected = mapOf("hello" to 1, "world" to 1)
        assertEquals(expected, result)
    }
}