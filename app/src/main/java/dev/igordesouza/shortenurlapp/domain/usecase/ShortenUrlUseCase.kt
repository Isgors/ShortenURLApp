package dev.igordesouza.shortenurlapp.domain.usecase

import dev.igordesouza.shortenurlapp.domain.model.Url
import dev.igordesouza.shortenurlapp.domain.repository.UrlRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ShortenUrlUseCase(private val urlRepository: UrlRepository) {
    operator fun invoke(url: String): Flow<Result<Url>> = flow {
        if (url.isBlank()) {
            emit(Result.failure(IllegalArgumentException("URL cannot be empty")))
            return@flow
        }

        if (urlRepository.findByOriginalUrl(url) != null) {
            emit(Result.failure(Exception("URL already exists")))
            return@flow
        }

        urlRepository.shortenUrl(url).collect {
            emit(it)
        }
    }
}
