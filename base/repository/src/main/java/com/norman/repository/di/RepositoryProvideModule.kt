package com.norman.repository.di

import com.norman.repository.articleRepository.ArticleRepository
import com.norman.repository.articleRepository.ArticleRepositoryImpl
import com.norman.repository.freshnessRepository.FreshnessRepository
import com.norman.repository.freshnessRepository.FreshnessRepositoryImpl
import com.norman.repository.networkRepository.NetworkRepository
import com.norman.repository.networkRepository.NetworkRepositoryImpl
import com.norman.repository.serviceCardRepository.ServiceCardRepository
import com.norman.repository.serviceCardRepository.ServiceCardRepositoryImpl
import com.norman.repository.weatherRepository.WeatherRepository
import com.norman.repository.weatherRepository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryProvideModule {

    @Binds
    @Singleton
    abstract fun bindArticleRepository(articleRepositoryImpl: ArticleRepositoryImpl): ArticleRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindServiceCardRepository(
        serviceCardRepositoryImpl: ServiceCardRepositoryImpl,
    ): ServiceCardRepository

    @Binds
    @Singleton
    abstract fun bindNetworkRepository(networkRepositoryImpl: NetworkRepositoryImpl): NetworkRepository

    @Binds
    @Singleton
    abstract fun bindFreshnessRepository(
        freshnessRepositoryImpl: FreshnessRepositoryImpl,
    ): FreshnessRepository
}
