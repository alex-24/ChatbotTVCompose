package com.applicassion.ChatbotTVCompose.di

import com.applicassion.ChatbotTVCompose.data.repository_impl.AppsRepositoryImpl
import com.applicassion.ChatbotTVCompose.domain.repository.AppsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppsModule {

    @Binds
    @Singleton
    abstract fun bindAppsRepository(
        impl: AppsRepositoryImpl
    ): AppsRepository
}

