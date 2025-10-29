package com.tc.example.domain.usecase.get15thCharacterUseCase

import com.tc.example.domain.usecase.get15thCharacterUseCase.Get15thCharacterUseCaseImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class Get15thCharacterUseCaseImplTest {

    private lateinit var useCase: Get15thCharacterUseCaseImpl

    @Before
    fun setup() {
        useCase = Get15thCharacterUseCaseImpl()
    }

    @Test
    fun `returns 15th character when text has at least 15 characters`() = runTest {
        // Arrange
        val text = "abcdefghijklmnop"

        // Act
        val result = useCase(text)

        // Assert
        assertEquals('o', result)
    }

    @Test
    fun `returns null when text is shorter than 15 characters`() = runTest {
        // Arrange
        val text = "short_text"

        // Act
        val result = useCase(text)

        // Assert
        assertNull(result)
    }

    @Test
    fun `returns 15th character correctly for long content`() = runTest {
        // Arrange
        val text = "abcdefghijklmnopqrstuvwxyz"

        // Act
        val result = useCase(text)

        // Assert
        assertEquals('o', result)
    }

    @Test
    fun `returns null for empty string`() = runTest {
        // Arrange
        val text = ""

        // Act
        val result = useCase(text)

        // Assert
        assertNull(result)
    }
}