package com.applicassion.ChatbotTVCompose.domain.utils

sealed class DomainResponse<out T> {
    data class Error(val throwable: Throwable): DomainResponse<Nothing>()
    data class Success<T>(val data: T): DomainResponse<T>()
}