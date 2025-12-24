package dev.igordesouza.shortenurlapp.domain.usecase

import dev.igordesouza.shortenurlapp.domain.repository.UrlRepository

class DeleteAllUrlsUseCaseImpl(
    private val urlRepository: UrlRepository
): DeleteAllUrlsUseCase {
    override suspend operator fun invoke() {
        urlRepository.deleteAllUrls()
    }
}
