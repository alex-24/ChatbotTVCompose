package com.applicassion.ChatbotTVCompose.data.remote.utils

import okhttp3.Interceptor
import okhttp3.Response

sealed class RequestHeaderInterceptors {
    class AuthBearerTokenInterceptor(private val authorizationToken: String): Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            return chain
                .request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $authorizationToken")
                .build()
                .let { chain.proceed(it) }
        }

    }
}