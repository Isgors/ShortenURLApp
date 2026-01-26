package dev.igordesouza.shortenurlapp.domain.usecase

import dev.igordesouza.shortenurlapp.domain.model.ShortenUrlOutcome
import dev.igordesouza.shortenurlapp.domain.repository.UrlRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ShortenUrlUseCaseImpl(
    private val urlRepository: UrlRepository
): ShortenUrlUseCase {

    override fun invoke(input: String): Flow<ShortenUrlOutcome> = flow {

        if (input.isBlank()) {
            emit(ShortenUrlOutcome.EmptyInput)
            return@flow
        }

        urlRepository.shortenUrl(input).collect { result ->
            result
                .onSuccess { emit(ShortenUrlOutcome.Success()) }
                .onFailure {
                    emit(
                        ShortenUrlOutcome.Error(
                            it.message ?: "Error shortening URL"
                        )
                    )
                }
        }
    }
}