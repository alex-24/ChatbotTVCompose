package com.applicassion.ChatbotTVCompose.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
class CloudflareModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CloudflareOkHttpClient


    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CloudflareRetrofit

    @Provides
    @CloudflareOkHttpClient
    fun provideCloudflareOkHttpClient(): OkHttpClient {

        return OkHttpClient.Builder().build()
    }

}