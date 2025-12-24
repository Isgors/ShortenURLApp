package dev.igordesouza.shortenurlapp.domain.usecase

import dev.igordesouza.shortenurlapp.domain.model.Url
import dev.igordesouza.shortenurlapp.domain.repository.UrlRepository

class GetRecentlyShortenedUrlsUseCase(private val urlRepository: UrlRepository) {
    suspend operator fun invoke(): List<Url> {
        return urlRepository.getRecentlyShortenedUrls()
    }
}
