package com.norman.repository.di

import com.norman.repository.articleRepository.ArticleRepository
import com.norman.repository.articleRepository.ArticleRepositoryImpl
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

}
