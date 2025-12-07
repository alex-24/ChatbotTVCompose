package com.applicassion.ChatbotTVCompose.domain.model

/**
 * Domain model for a chat message
 */
data class ChatMessage(
    val role: Role,
    val content: String
) {
    enum class Role {
        SYSTEM,
        USER,
        ASSISTANT
    }
}

/**
 * Domain model for text generation result
 */
data class TextGenerationResult(
    val response: String
)

