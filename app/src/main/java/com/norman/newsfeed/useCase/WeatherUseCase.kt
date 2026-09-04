package com.norman.newsfeed.useCase

import com.norman.model.api.weather.response.WeatherResponse
import com.norman.newsfeed.pojo.WeatherBO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherUseCase @Inject constructor() {

    fun convertWeatherResponseToWeatherBO(
        response: WeatherResponse,
        updateTime: Long = System.currentTimeMillis(),
    ): WeatherBO {
        return WeatherBO(
            temperature = response.current.temperature,
            apparentTemperature = response.current.apparentTemperature,
            temperatureUnit = response.currentUnits.temperature,
            weatherCode = response.current.weatherCode,
            isDay = response.current.isDay == 1,
            updateTime = updateTime,
        )
    }
}
