package dev.igordesouza.shortenurlapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AliasRequest(
    val url: String
)