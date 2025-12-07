package com.applicassion.ChatbotTVCompose.domain.service

/**
 * Generic AI service interface defining available models with their standard IDs.
 */
interface AIService {
    
    /** Speech-to-text models */
    enum class SpeechToTextModel(val modelId: String) {
        WHISPER("openai/whisper")
    }
    
    /** Text generation models */
    enum class TextGenerationModel(val modelId: String) {
        LLAMA_8B_INSTRUCT("meta/llama-3.1-8b-instruct-fast")
    }
}

