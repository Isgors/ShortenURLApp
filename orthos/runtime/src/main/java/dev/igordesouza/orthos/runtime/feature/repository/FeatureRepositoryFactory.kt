package dev.igordesouza.orthos.runtime.feature.repository

import android.content.Context
import dev.igordesouza.orthos.runtime.feature.datasource.LocalFeatureDataSourceImpl
import dev.igordesouza.orthos.runtime.feature.datasource.RemoteFeatureDataSourceImpl
import dev.igordesouza.orthos.runtime.feature.datasource.*
import kotlinx.serialization.json.Json

/**
 * Factory responsible for wiring repository dependencies.
 */

object FeatureRepositoryFactory {

    fun create(
        context: Context,
        remoteJsonProvider: RemoteFeatureJsonProvider? = null
    ): FeatureRepository {

        val json = Json {
            ignoreUnknownKeys = true
        }

        val jsonProvider = remoteJsonProvider
            ?: RemoteFeatureJsonProviderImpl(context)

        val remoteDataSource: RemoteFeatureDataSource =
            RemoteFeatureDataSourceImpl(
                jsonProvider = jsonProvider,
                json = json
            )

        val localDataSource: LocalFeatureDataSource =
            LocalFeatureDataSourceImpl(
                context = context,
                json = json
            )

        return FeatureRepositoryImpl(
            remote = remoteDataSource,
            local = localDataSource
        )
    }
}

