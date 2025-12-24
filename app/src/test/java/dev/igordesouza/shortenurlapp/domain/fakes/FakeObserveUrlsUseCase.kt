package dev.igordesouza.shortenurlapp.domain.fakes

import dev.igordesouza.shortenurlapp.domain.model.Url
import dev.igordesouza.shortenurlapp.domain.usecase.ObserveUrlsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeObserveUrlsUseCase : ObserveUrlsUseCase {

    private val flow = MutableStateFlow<List<Url>>(emptyList())

    fun emit(urls: List<Url>) {
        flow.value = urls
    }

    override fun invoke(): Flow<List<Url>> = flow
}
