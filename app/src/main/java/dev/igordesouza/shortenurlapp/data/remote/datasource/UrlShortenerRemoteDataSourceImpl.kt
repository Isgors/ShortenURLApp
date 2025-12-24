package dev.igordesouza.shortenurlapp.data.remote.datasource

import dev.igordesouza.shortenurlapp.data.remote.model.AliasRequest
import dev.igordesouza.shortenurlapp.data.remote.model.AliasResponse
import dev.igordesouza.shortenurlapp.data.remote.network.UrlShortenerService

class UrlShortenerRemoteDataSourceImpl(private val urlShortenerService: UrlShortenerService) :
    UrlShortenerRemoteDataSource {
    override suspend fun shortenUrl(url: String): AliasResponse {
        return urlShortenerService.postAlias(AliasRequest(url))
    }
}
