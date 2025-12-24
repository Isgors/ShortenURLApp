package dev.igordesouza.shortenurlapp.data.remote.datasource

import dev.igordesouza.shortenurlapp.data.remote.model.AliasResponse

interface UrlShortenerRemoteDataSource {
    suspend fun shortenUrl(url: String): AliasResponse
}
