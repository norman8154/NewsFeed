package com.norman.repository.weatherRepository

import com.norman.model.api.weather.response.WeatherResponse
import com.norman.retrofit.api.WeatherApi
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
) : WeatherRepository {

    override suspend fun fetchWeather(
        latitude: Float,
        longitude: Float,
    ): Result<WeatherResponse> = runCatching {
        weatherApi.fetchWeather(latitude, longitude)
    }
}
