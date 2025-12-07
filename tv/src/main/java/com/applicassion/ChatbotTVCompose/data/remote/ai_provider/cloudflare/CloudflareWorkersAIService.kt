package com.applicassion.ChatbotTVCompose.data.remote.ai_provider.cloudflare

import com.applicassion.ChatbotTVCompose.data.remote.ai_provider.cloudflare.dto.speech_to_text.CFWhisperEnvelopeDTO
import com.applicassion.ChatbotTVCompose.domain.service.AIService
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Cloudflare Workers AI Retrofit service.
 * Uses standard model IDs from AIService.
 */
interface CloudflareWorkersAIService : AIService {

    @POST("{modelId}")
    suspend fun speechToText(
        @Path("modelId", encoded = true) modelId: String,
        @Body audioCapture: RequestBody
    ): CFWhisperEnvelopeDTO

    @POST("{modelId}")
    suspend fun textGeneration(
        @Path("modelId", encoded = true) modelId: String,
        @Body prompt: String
    ): String // TODO: replace with concrete response model
}