package com.tc.example.data.di

import com.tc.example.data.client.KtorClientProvider
import com.tc.example.data.repository.ContentRepositoryImpl
import com.tc.example.domain.repository.ContentRepository
import com.tc.example.domain.usecase.get15thCharacterUseCase.Get15thCharacterUseCase
import com.tc.example.domain.usecase.get15thCharacterUseCase.Get15thCharacterUseCaseImpl
import com.tc.example.domain.usecase.getContentUseCase.GetContentUseCase
import com.tc.example.domain.usecase.getContentUseCase.GetContentUseCaseImpl
import com.tc.example.domain.usecase.getEvery15thCharacterUseCase.GetEvery15thCharacterUseCase
import com.tc.example.domain.usecase.getEvery15thCharacterUseCase.GetEvery15thCharacterUseCaseImpl
import com.tc.example.domain.usecase.getWordCountsUseCase.GetWordCountsUseCase
import com.tc.example.domain.usecase.getWordCountsUseCase.GetWordCountsUseCaseImpl
import org.koin.dsl.module

/**
 * Koin module for data layer dependencies.
 */
val dataModule = module {

    // Ktor HttpClient
    single { KtorClientProvider.create() }

    // Repository
    single<ContentRepository> { ContentRepositoryImpl(get()) }

    // Use case implementations
    single<GetContentUseCase> { GetContentUseCaseImpl(get()) }
    single<Get15thCharacterUseCase> { Get15thCharacterUseCaseImpl() }
    single<GetEvery15thCharacterUseCase> { GetEvery15thCharacterUseCaseImpl() }
    single<GetWordCountsUseCase> { GetWordCountsUseCaseImpl() }
}