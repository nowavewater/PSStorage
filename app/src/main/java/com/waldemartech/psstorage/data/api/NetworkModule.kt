package com.waldemartech.psstorage.data.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor {
            Timber.i(it)
        }.apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return logging
    }

    @Provides
    @Singleton
    fun provideOkHttpCallFactory(
        logging: HttpLoggingInterceptor
    ): Call.Factory  {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .callTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiDataSource(
        okhttpCallFactory: Call.Factory,
    ): ApiDataSource{
        return Retrofit.Builder()
            .baseUrl("https://google.com")
            .callFactory(okhttpCallFactory)
            .build()
            .create(ApiDataSource::class.java)
    }


}