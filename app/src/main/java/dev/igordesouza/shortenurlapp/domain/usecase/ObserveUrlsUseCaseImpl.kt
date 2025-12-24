package dev.igordesouza.shortenurlapp.domain.usecase

import dev.igordesouza.shortenurlapp.domain.model.Url
import dev.igordesouza.shortenurlapp.domain.repository.UrlRepository
import kotlinx.coroutines.flow.Flow

class ObserveUrlsUseCaseImpl(
    private val repository: UrlRepository
) : ObserveUrlsUseCase {

    override fun invoke(): Flow<List<Url>> =
        repository.observeUrls()
}
