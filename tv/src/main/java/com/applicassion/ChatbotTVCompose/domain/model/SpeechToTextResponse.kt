package com.applicassion.ChatbotTVCompose.domain.model

/**
 * Domain model for speech-to-text result.
 * Clean data class with no framework dependencies.
 */
data class SpeechToTextResult(
    val text: String,
    val wordCount: Int,
    val words: List<SpeechToTextWord>,
    val vtt: String
)

data class SpeechToTextWord(
    val word: String,
    val startSeconds: Double,
    val endSeconds: Double
)
