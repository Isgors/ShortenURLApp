package dev.igordesouza.shortenurlapp.domain.usecase

import dev.igordesouza.shortenurlapp.domain.model.Url
import kotlinx.coroutines.flow.Flow

interface ObserveUrlsUseCase {
    operator fun invoke(): Flow<List<Url>>
}
