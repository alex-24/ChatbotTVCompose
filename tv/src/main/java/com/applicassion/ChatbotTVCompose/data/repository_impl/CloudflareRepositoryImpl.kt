package com.applicassion.ChatbotTVCompose.data.repository_impl

import android.content.Context
import android.util.Log
import com.applicassion.ChatbotTVCompose.data.remote.ai_provider.cloudflare.CloudflareWorkersBaseAIService
import com.applicassion.ChatbotTVCompose.domain.model.SpeechToTextResult
import com.applicassion.ChatbotTVCompose.domain.repository.AIRepository
import com.applicassion.ChatbotTVCompose.data.remote.ai_provider.BaseAIService
import com.applicassion.ChatbotTVCompose.domain.utils.DomainResponse
import com.applicassion.ChatbotTVCompose.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject

class CloudflareRepositoryImpl @Inject constructor(
    private val cloudflareWorkersAIService: CloudflareWorkersBaseAIService,
    @ApplicationContext private val context: Context
): AIRepository {
    companion object {
        private const val TAG = "CloudflareRepositoryImpl"
        private const val DEBUG_SAVE_AUDIO = false
    }

    override suspend fun speechToText(
        model: BaseAIService.SpeechToTextModel,
        audioCapture: ByteArray
    ): DomainResponse<SpeechToTextResult> {
        return try {
            val wavBytes = buildWavFromPcm(
                pcmData = audioCapture,
                sampleRate = Constants.Audio.SAMPLE_RATE_HZ
            )
            
            if (DEBUG_SAVE_AUDIO) {
                saveDebugWavFile(wavBytes)
            }
            
            val requestBody = wavBytes.toRequestBody(Constants.Audio.MEDIA_TYPE_WAV.toMediaType())
            val envelopeDTO = cloudflareWorkersAIService.speechToText(
                modelId = model.modelId,
                audioCapture = requestBody
            )
            
            // Map DTO to domain model
            val result = envelopeDTO.result.toDomain()
            Log.d(TAG, "Speech-to-text result: '${result.text}'")
            DomainResponse.Success(data = result)
        } catch (e: Exception) {
            Log.e(TAG, "Speech-to-text failed", e)
            DomainResponse.Error(e)
        }
    }

    /**
     * Wrap raw PCM 16-bit mono data into a minimal WAV container so the API
     * receives a properly formatted audio/wav payload.
     */
    private fun buildWavFromPcm(
        pcmData: ByteArray,
        sampleRate: Int,
        channels: Short = Constants.Audio.CHANNELS,
        bitsPerSample: Short = Constants.Audio.BITS_PER_SAMPLE
    ): ByteArray {
        val headerSize = Constants.Audio.WAV_HEADER_SIZE
        val byteRate = sampleRate * channels * bitsPerSample / 8
        val blockAlign = (channels * bitsPerSample / 8).toShort()
        val dataSize = pcmData.size
        val totalDataLen = dataSize + headerSize - 8

        val buffer = ByteBuffer.allocate(headerSize + dataSize)
        buffer.order(ByteOrder.LITTLE_ENDIAN)

        buffer.put("RIFF".toByteArray())
        buffer.putInt(totalDataLen)
        buffer.put("WAVE".toByteArray())
        buffer.put("fmt ".toByteArray())
        buffer.putInt(16) // Subchunk1Size for PCM
        buffer.putShort(1) // Audio format PCM = 1
        buffer.putShort(channels)
        buffer.putInt(sampleRate)
        buffer.putInt(byteRate)
        buffer.putShort(blockAlign)
        buffer.putShort(bitsPerSample)
        buffer.put("data".toByteArray())
        buffer.putInt(dataSize)
        buffer.put(pcmData)

        return buffer.array()
    }
    
    /**
     * Save WAV file to app's cache directory for debugging.
     * File will be at: /data/data/com.applicassion.ChatbotTVCompose/cache/debug_audio_TIMESTAMP.wav
     * 
     * To pull the file via adb:
     * adb shell "run-as com.applicassion.ChatbotTVCompose cat /data/data/com.applicassion.ChatbotTVCompose/cache/debug_audio_*.wav" > debug.wav
     */
    private fun saveDebugWavFile(wavBytes: ByteArray) {
        try {
            val timestamp = System.currentTimeMillis()
            val file = File(context.cacheDir, "debug_audio_$timestamp.wav")
            FileOutputStream(file).use { fos ->
                fos.write(wavBytes)
            }
            Log.d(TAG, "DEBUG: Saved WAV file to: ${file.absolutePath}")
            Log.d(TAG, "DEBUG: Pull with: adb shell \"run-as com.applicassion.ChatbotTVCompose cat ${file.absolutePath}\" > debug.wav")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save debug WAV file", e)
        }
    }
}