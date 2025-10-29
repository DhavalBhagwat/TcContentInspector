package com.tc.example.domain.usecase.getContentUseCase

import com.tc.example.domain.model.ContentResponse
import com.tc.example.domain.repository.ContentRepository
import com.tc.example.domain.usecase.getContentUseCase.GetContentUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetContentUseCaseImplTest {

    private lateinit var repository: ContentRepository
    private lateinit var useCase: GetContentUseCaseImpl

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetContentUseCaseImpl(repository)
    }

    @Test
    fun `invokes repository and returns content successfully`() = runTest {
        // Arrange
        val expectedResponse = ContentResponse(content = "Sample text from site")
        coEvery { repository.fetchContent() } returns expectedResponse

        // Act
        val result = useCase()

        // Assert
        assertEquals(expectedResponse, result)
        coVerify(exactly = 1) { repository.fetchContent() }
    }

    @Test(expected = Exception::class)
    fun `propagates exception when repository throws`() = runTest {
        // Arrange
        coEvery { repository.fetchContent() } throws Exception("Network failure")

        // Act
        useCase()
    }
}