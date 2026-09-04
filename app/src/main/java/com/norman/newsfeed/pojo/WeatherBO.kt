package com.norman.newsfeed.pojo

data class WeatherBO(
    val temperature: Double,
    val apparentTemperature: Double,
    val temperatureUnit: String,
    val weatherCode: Int,
    val isDay: Boolean,
    val updateTime: Long,
)
