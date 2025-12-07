package com.applicassion.ChatbotTVCompose.domain.repository

import com.applicassion.ChatbotTVCompose.domain.model.SpeechToTextResult
import com.applicassion.ChatbotTVCompose.domain.service.AIService
import com.applicassion.ChatbotTVCompose.domain.utils.DomainResponse

interface AIRepository {
    suspend fun speechToText(
        model: AIService.SpeechToTextModel,
        audioCapture: ByteArray
    ): DomainResponse<SpeechToTextResult>
}