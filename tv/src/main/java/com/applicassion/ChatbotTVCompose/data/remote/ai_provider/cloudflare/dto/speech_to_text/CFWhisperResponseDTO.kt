package com.applicassion.ChatbotTVCompose.data.remote.ai_provider.cloudflare.dto.speech_to_text

import com.applicassion.ChatbotTVCompose.domain.model.SpeechToTextResult
import com.applicassion.ChatbotTVCompose.domain.model.SpeechToTextWord
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

/**
 * Cloudflare Workers AI envelope wrapper.
 * All Cloudflare AI responses are wrapped in this structure.
 */
@Serializable
data class CFEnvelopeDTO<T>(
    val result: T,
    val success: Boolean,
    val errors: List<CFErrorDTO> = emptyList(),
    val messages: List<String> = emptyList()
)

@Serializable
data class CFErrorDTO(
    val code: Int? = null,
    val message: String? = null
)

/**
 * Whisper speech-to-text response DTO.
 */
@Serializable
data class CFWhisperResponseDTO(
    val text: String,
    @SerialName("word_count")
    val wordCount: Int,
    @Serializable(with = CFWhisperWordsSerializer::class)
    val words: List<CFWhisperWordDTO>,
    val vtt: String
) {
    fun toDomain(): SpeechToTextResult = SpeechToTextResult(
        text = text,
        wordCount = wordCount,
        words = words.map { it.toDomain() },
        vtt = vtt
    )
}

@Serializable
data class CFWhisperWordDTO(
    val word: String,
    val start: Double,
    val end: Double
) {
    fun toDomain(): SpeechToTextWord = SpeechToTextWord(
        word = word,
        startSeconds = start,
        endSeconds = end
    )
}

/**
 * Cloudflare sometimes returns `words` as a single object instead of an array.
 * This serializer handles both cases:
 *   "words": { ... }        -> single word
 *   "words": [ { ... }, ... ] -> array of words
 */
object CFWhisperWordsSerializer : KSerializer<List<CFWhisperWordDTO>> {
    private val delegate = ListSerializer(CFWhisperWordDTO.serializer())

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun deserialize(decoder: Decoder): List<CFWhisperWordDTO> {
        val jsonDecoder = decoder as? JsonDecoder
            ?: error("CFWhisperWordsSerializer can only be used with Json")
        val element = jsonDecoder.decodeJsonElement()

        return when (element) {
            is JsonArray -> jsonDecoder.json.decodeFromJsonElement(delegate, element)
            is JsonObject -> listOf(
                jsonDecoder.json.decodeFromJsonElement(CFWhisperWordDTO.serializer(), element)
            )
            else -> emptyList()
        }
    }

    override fun serialize(encoder: Encoder, value: List<CFWhisperWordDTO>) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: error("CFWhisperWordsSerializer can only be used with Json")
        jsonEncoder.encodeSerializableValue(delegate, value)
    }
}

/** Type alias for the complete Whisper API response */
typealias CFWhisperEnvelopeDTO = CFEnvelopeDTO<CFWhisperResponseDTO>
