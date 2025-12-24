package dev.igordesouza.shortenurlapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AliasResponse(
    val alias: String,
    @SerialName("_links")
    val links: Links
)

@Serializable
data class Links(
    @SerialName("self")
    val originalUrl: String,
    @SerialName("short")
    val shortUrl: String
)
