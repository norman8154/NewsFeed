package com.norman.model.api.weather.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("latitude")
    val latitude: Float,
    @SerialName("longitude")
    val longitude: Float,
    @SerialName("utc_offset_seconds")
    val utcOffsetSeconds: Long,
    @SerialName("timezone")
    val timezone: String,
    @SerialName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    @SerialName("elevation")
    val elevation: Float,
    @SerialName("current_units")
    val currentUnits: CurrentWeatherUnitsResponse,
    @SerialName("current")
    val current: CurrentWeatherResponse,
)

@Serializable
data class CurrentWeatherUnitsResponse(
    @SerialName("time")
    val time: String,
    @SerialName("interval")
    val interval: String,
    @SerialName("temperature_2m")
    val temperature: String,
    @SerialName("apparent_temperature")
    val apparentTemperature: String,
    @SerialName("weather_code")
    val weatherCode: String,
    @SerialName("is_day")
    val isDay: String,
)

@Serializable
data class CurrentWeatherResponse(
    @SerialName("time")
    val time: String,
    @SerialName("interval")
    val interval: Int,
    @SerialName("temperature_2m")
    val temperature: Double,
    @SerialName("apparent_temperature")
    val apparentTemperature: Double,
    @SerialName("weather_code")
    val weatherCode: Int,
    @SerialName("is_day")
    val isDay: Int,
)
