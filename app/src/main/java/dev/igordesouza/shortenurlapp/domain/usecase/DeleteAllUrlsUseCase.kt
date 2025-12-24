package dev.igordesouza.shortenurlapp.domain.usecase

import dev.igordesouza.shortenurlapp.domain.repository.UrlRepository

class DeleteAllUrlsUseCase(private val urlRepository: UrlRepository) {
    suspend operator fun invoke() {
        urlRepository.deleteAllUrls()
    }
}
