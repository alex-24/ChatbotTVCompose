package com.applicassion.ChatbotTVCompose.utils

sealed class Constants {

    object Audio {
        const val SAMPLE_RATE_HZ: Int = 16_000
        const val MAX_RECORD_DURATION_MS: Long = 10_000L
        const val MIN_AUDIO_BYTES: Int = 1_000 // Minimum bytes for valid recording (~30ms)
        const val MIN_BUFFER_SIZE: Int = 8_192
        const val BUFFER_SIZE_MULTIPLIER: Int = 2
        const val CHANNELS: Short = 1 // Mono
        const val BITS_PER_SAMPLE: Short = 16
        const val WAV_HEADER_SIZE: Int = 44
        const val MEDIA_TYPE_WAV: String = "audio/wav"
    }

    object Dimen {
        const val FOCUS_SCALE_FACTOR: Float = 1.2f
    }
}