package com.norman.retrofit.api

import com.norman.model.api.weather.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast")
    suspend fun fetchWeather(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("current") current: String = CURRENT_FIELDS,
        @Query("timezone") timezone: String = "auto",
    ): WeatherResponse

    companion object {
        const val CURRENT_FIELDS =
            "temperature_2m,apparent_temperature,weather_code,is_day"
    }
}
