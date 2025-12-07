package com.applicassion.ChatbotTVCompose.data.remote.ai_provider.cloudflare.dto.textgen

import com.applicassion.ChatbotTVCompose.domain.model.ChatMessage
import com.applicassion.ChatbotTVCompose.domain.model.TextGenerationResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request body for Cloudflare Llama text generation.
 * Based on: https://developers.cloudflare.com/workers-ai/models/llama-3.1-8b-instruct-fast/
 */
@Serializable
data class CFLlamaRequestDTO(
    val messages: List<CFMessageDTO>,
    val stream: Boolean = false,
    @SerialName("max_tokens")
    val maxTokens: Int = 256,
    val temperature: Double = 0.6
)

@Serializable
data class CFMessageDTO(
    val role: String, // "system", "user", "assistant"
    val content: String
) {
    companion object {
        fun fromDomain(message: ChatMessage): CFMessageDTO = CFMessageDTO(
            role = when (message.role) {
                ChatMessage.Role.SYSTEM -> "system"
                ChatMessage.Role.USER -> "user"
                ChatMessage.Role.ASSISTANT -> "assistant"
            },
            content = message.content
        )
    }
}

/**
 * Response envelope from Cloudflare Llama API
 */
@Serializable
data class CFLlamaEnvelopeDTO(
    val result: CFLlamaResponseDTO? = null,
    val success: Boolean,
    val errors: List<CFLlamaErrorDTO> = emptyList(),
    val messages: List<String> = emptyList()
)

@Serializable
data class CFLlamaResponseDTO(
    val response: String,
    @SerialName("tool_calls")
    val toolCalls: List<CFToolCallDTO>? = null
) {
    fun toDomain(): TextGenerationResult = TextGenerationResult(
        response = response
    )
}

@Serializable
data class CFToolCallDTO(
    val name: String,
    val arguments: Map<String, String>? = null
)

@Serializable
data class CFLlamaErrorDTO(
    val code: Int? = null,
    val message: String
)
