package com.norman.retrofit.di

import com.norman.retrofit.BuildConfig
import com.norman.retrofit.api.ArticleApi
import com.norman.retrofit.api.ServiceCardApi
import com.norman.retrofit.api.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitProvideModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient
            .Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
        }

        return builder.build()
    }

    @Provides
    @Singleton
    @Named("article")
    fun provideArticleRetrofit(
        json: Json,
        okHttpClient: OkHttpClient,
    ): Retrofit = createRetrofit(BuildConfig.NEWS_URL, json, okHttpClient)

    @Provides
    @Singleton
    @Named("weather")
    fun provideWeatherRetrofit(
        json: Json,
        okHttpClient: OkHttpClient,
    ): Retrofit = createRetrofit(BuildConfig.WEATHER_URL, json, okHttpClient)

    @Provides
    @Singleton
    @Named("serviceCard")
    fun provideServiceCardRetrofit(
        json: Json,
        okHttpClient: OkHttpClient,
    ): Retrofit = createRetrofit(BuildConfig.SERVICE_URL, json, okHttpClient)

    @Provides
    @Singleton
    fun provideArticleApi(@Named("article") retrofit: Retrofit): ArticleApi = retrofit.create(ArticleApi::class.java)

    @Provides
    @Singleton
    fun provideWeatherApi(@Named("weather") retrofit: Retrofit): WeatherApi = retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideServiceCardApi(
        @Named("serviceCard") retrofit: Retrofit,
    ): ServiceCardApi = retrofit.create(ServiceCardApi::class.java)

    private fun createRetrofit(
        baseUrl: String,
        json: Json,
        okHttpClient: OkHttpClient,
    ): Retrofit {
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
