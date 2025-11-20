@file:Suppress("TooManyFunctions")

package io.mityukov.simpla.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mityukov.geo.tracking.core.data.repository.geo.GeolocationProvider
import io.mityukov.geo.tracking.core.data.repository.geo.HardwareGeolocationProviderImpl
import io.mityukov.simpla.core.domain.training.TrainingController
import io.mityukov.simpla.core.data.TrainingControllerImpl
import io.mityukov.simpla.core.data.repository.training.SelectedTrainingRepository
import io.mityukov.simpla.core.data.repository.training.SelectedTrainingRepositoryImpl
import io.mityukov.simpla.core.data.repository.training.TrainingRemoteDataSource
import io.mityukov.simpla.core.data.repository.training.TrainingRemoteDataSourceImpl
import io.mityukov.simpla.core.data.repository.training.TrainingRepository
import io.mityukov.simpla.core.data.repository.training.TrainingRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Singleton
    @Binds
    internal abstract fun bindTrainingRepository(impl: TrainingRepositoryImpl): TrainingRepository

    @Binds
    internal abstract fun bindsTrainingRemoteDataSource(impl: TrainingRemoteDataSourceImpl): TrainingRemoteDataSource

    @Singleton
    @Binds
    internal abstract fun bindSelectedTrainingRepository(impl: SelectedTrainingRepositoryImpl): SelectedTrainingRepository

    @Singleton
    @Binds
    internal abstract fun bindTrainingController(impl: TrainingControllerImpl): TrainingController

    @Binds
    internal abstract fun bindGeolocationProvider(impl: HardwareGeolocationProviderImpl): GeolocationProvider
}
