package com.applicassion.ChatbotTVCompose.di

import android.content.Context
import com.applicassion.ChatbotTVCompose.data.remote.utils.RequestHeaderInterceptors
import com.applicassion.ChatbotTVCompose.BuildConfig
import com.applicassion.ChatbotTVCompose.data.remote.ai_provider.cloudflare.CloudflareWorkersAIService
import com.applicassion.ChatbotTVCompose.data.repository_impl.CloudflareRepositoryImpl
import com.applicassion.ChatbotTVCompose.domain.repository.AIRepository
import com.applicassion.ChatbotTVCompose.domain.usecase.SpeechToTextUseCase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

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
    @Singleton
    @CloudflareOkHttpClient
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(
                when(BuildConfig.DEBUG) {
                    true -> HttpLoggingInterceptor.Level.BODY
                    false -> HttpLoggingInterceptor.Level.BASIC
                }
            )
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(RequestHeaderInterceptors.AuthBearerTokenInterceptor(BuildConfig.CLOUDFLARE_AUTH_TOKEN))
            .callTimeout(20.seconds)
            .build()
    }

    @Provides
    @Singleton
    @CloudflareRetrofit
    fun provideRetrofit(@CloudflareOkHttpClient okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl("https://api.cloudflare.com/client/v4/accounts/${BuildConfig.CLOUDFLARE_ACCOUNT_ID}/ai/run/@cf/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideCloudflareWorkersAiService(@CloudflareRetrofit retrofit: Retrofit): CloudflareWorkersAIService {
        return retrofit.create(CloudflareWorkersAIService::class.java)
    }

    @Provides
    @Singleton
    fun provideAIRepository(
        service: CloudflareWorkersAIService,
        @ApplicationContext context: Context
    ): AIRepository {
        return CloudflareRepositoryImpl(
            cloudflareWorkersAIService = service,
            context = context
        )
    }

    @Provides
    @Singleton
    fun provideSpeechToTextUseCase(repository: AIRepository): SpeechToTextUseCase {
        return SpeechToTextUseCase(repository = repository)
    }

}