package com.applicassion.ChatbotTVCompose.domain.usecase

import com.applicassion.ChatbotTVCompose.domain.model.SpeechToTextResult
import com.applicassion.ChatbotTVCompose.domain.repository.AIRepository
import com.applicassion.ChatbotTVCompose.domain.service.AIService
import com.applicassion.ChatbotTVCompose.domain.utils.DomainResponse

class SpeechToTextUseCase(val repository: AIRepository) {
    suspend operator fun invoke(
        model: AIService.SpeechToTextModel,
        audioCapture: ByteArray
    ): DomainResponse<SpeechToTextResult> {
        return repository.speechToText(
            model = model,
            audioCapture = audioCapture
        )
    }
}