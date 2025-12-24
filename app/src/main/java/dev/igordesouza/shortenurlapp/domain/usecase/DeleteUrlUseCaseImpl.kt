package dev.igordesouza.shortenurlapp.domain.usecase

import dev.igordesouza.shortenurlapp.domain.model.Url
import dev.igordesouza.shortenurlapp.domain.repository.UrlRepository

class DeleteUrlUseCaseImpl(
    private val urlRepository: UrlRepository
): DeleteUrlUseCase {
    override suspend operator fun invoke(url: Url) {
        urlRepository.deleteUrl(url)
    }
}
