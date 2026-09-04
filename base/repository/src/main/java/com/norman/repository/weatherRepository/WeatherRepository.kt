package com.norman.repository.weatherRepository

import com.norman.model.api.weather.response.WeatherResponse

interface WeatherRepository {

    suspend fun fetchWeather(
        latitude: Float,
        longitude: Float,
    ): Result<WeatherResponse>
}
