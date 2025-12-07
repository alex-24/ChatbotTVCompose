package com.applicassion.ChatbotTVCompose.domain.usecase

import com.applicassion.ChatbotTVCompose.data.remote.ai_provider.BaseAIService
import com.applicassion.ChatbotTVCompose.domain.model.ChatMessage
import com.applicassion.ChatbotTVCompose.domain.model.TextGenerationResult
import com.applicassion.ChatbotTVCompose.domain.repository.AIRepository
import com.applicassion.ChatbotTVCompose.domain.utils.DomainResponse
import javax.inject.Inject

class TextGenerationUseCase @Inject constructor(
    private val aiRepository: AIRepository
) {
    suspend operator fun invoke(
        model: BaseAIService.TextGenerationModel,
        messages: List<ChatMessage>
    ): DomainResponse<TextGenerationResult> {
        return aiRepository.textGeneration(model, messages)
    }
}

