package dev.igordesouza.shortenurlapp.data.remote.network

import dev.igordesouza.shortenurlapp.BuildConfig
import dev.igordesouza.shortenurlapp.data.remote.model.AliasRequest
import dev.igordesouza.shortenurlapp.data.remote.model.AliasResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UrlShortenerService(private val client: HttpClient) {

    suspend fun postAlias(request: AliasRequest): AliasResponse {
        return client.post("${BuildConfig.BASE_URL}api/alias") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
