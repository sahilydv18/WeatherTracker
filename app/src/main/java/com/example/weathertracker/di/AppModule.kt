package com.example.weathertracker.di

import com.example.weathertracker.remote.WeatherApi
import com.example.weathertracker.remote.repository.WeatherRepo
import com.example.weathertracker.remote.repository.WeatherRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://api.weatherapi.com/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepo(weatherApi: WeatherApi): WeatherRepo {
        return WeatherRepoImpl(weatherApi)
    }
}