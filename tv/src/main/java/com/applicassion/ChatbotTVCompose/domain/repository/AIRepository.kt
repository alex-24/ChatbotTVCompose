package com.applicassion.ChatbotTVCompose.domain.repository

import com.applicassion.ChatbotTVCompose.domain.model.SpeechToTextResult
import com.applicassion.ChatbotTVCompose.data.remote.ai_provider.BaseAIService
import com.applicassion.ChatbotTVCompose.domain.utils.DomainResponse

interface AIRepository {
    suspend fun speechToText(
        model: BaseAIService.SpeechToTextModel,
        audioCapture: ByteArray
    ): DomainResponse<SpeechToTextResult>
}