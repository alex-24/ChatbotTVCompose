package com.applicassion.ChatbotTVCompose.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.annotation.RequiresPermission
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import java.util.concurrent.atomic.AtomicBoolean
import com.applicassion.ChatbotTVCompose.data.remote.ai_provider.BaseAIService
import com.applicassion.ChatbotTVCompose.domain.usecase.SpeechToTextUseCase
import com.applicassion.ChatbotTVCompose.domain.utils.DomainResponse
import com.applicassion.ChatbotTVCompose.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

enum class ConversationRole {
    CHAT_BOT,
    USER
}

@HiltViewModel
class VocalAssistantViewModel @Inject constructor(
    application: Application,
    val speechToTextUseCase: SpeechToTextUseCase
): AndroidViewModel(application) {

    private val _vocalAssistantUIState: MutableLiveData<VocalAssistantUIState> = MutableLiveData(
        VocalAssistantUIState(isRecording = false, error = null)
    )
    val vocalAssistantUIState: LiveData<VocalAssistantUIState>
        get() = _vocalAssistantUIState

    private val _conversation = mutableStateListOf<Pair<ConversationRole, String>>()
    val conversation: List<Pair<ConversationRole, String>>
        get() = _conversation

    // Thread-safe
    private val _isRecordingFlag = AtomicBoolean(false)
    
    private val isRecordingAudio: Boolean
        get() = _isRecordingFlag.get()

    private var audioRecordingJob: Job? = null
    
    companion object {
        private const val TAG = "VocalAssistantViewModel"
    }

    init {
        resetConversation()
    }


    fun resetConversation() {
        _isRecordingFlag.set(false)
        audioRecordingJob?.cancel()
        _conversation.clear()
        _vocalAssistantUIState.value = VocalAssistantUIState(isRecording = false, error = null)
        addConversationElement(
            role = ConversationRole.CHAT_BOT,
            text = "Hi, how can I help you?"
        )
    }



    private suspend fun convertSpeechToText(audioCapture: ByteArray): String? {
        val result = speechToTextUseCase(
            model = BaseAIService.SpeechToTextModel.WHISPER,
            audioCapture = audioCapture
        )

        return when (result) {
            is DomainResponse.Error -> null
            is DomainResponse.Success -> result.data.text
        }
    }

    fun onMicClickWithPermission() {
        toggleAudioRecording()
    }

    fun onMicPermissionResult(granted: Boolean) {
        if (granted) {
            onMicClickWithPermission()
        } else {
            _vocalAssistantUIState.value = _vocalAssistantUIState.value!!.copy(
                isRecording = false,
                error = "Please grant the microphone usage permission to use the vocal chatbot"
            )
        }
    }

    private fun toggleAudioRecording() {
        if (isRecordingAudio) {
            stopAudioRecording()
        } else {
            startAudioRecording()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startAudioRecording() {
        if (!isRecordingAudio) {
            _isRecordingFlag.set(true)
            _vocalAssistantUIState.value = _vocalAssistantUIState.value!!.copy(
                isRecording = true,
                error = null
            )
            
            audioRecordingJob = viewModelScope.launch(Dispatchers.IO) {
                val rawBytes = recordAudioToBytes()
                
                if (rawBytes != null && rawBytes.size > Constants.Audio.MIN_AUDIO_BYTES) {
                    val text = convertSpeechToText(audioCapture = rawBytes)
                    
                    withContext(Dispatchers.Main) {
                        if (text != null) {
                            addConversationElement(
                                role = ConversationRole.USER,
                                text = text
                            )
                            _vocalAssistantUIState.value = _vocalAssistantUIState.value!!.copy(
                                isRecording = false,
                                error = null
                            )
                        } else {
                            _vocalAssistantUIState.value = _vocalAssistantUIState.value!!.copy(
                                isRecording = false,
                                error = "No words detected in audio recording"
                            )
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _vocalAssistantUIState.value = _vocalAssistantUIState.value?.copy(
                            isRecording = false,
                            error = "Recording too short. Please hold longer."
                        )
                    }
                }
            }
        }
    }

    private fun addConversationElement(role: ConversationRole, text: String) {
        _conversation.add(Pair(role, text))
    }

    private fun stopAudioRecording() {
        if (isRecordingAudio) {
            _isRecordingFlag.set(false)
        }
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private suspend fun recordAudioToBytes(
        maxRecordDurationMillis: Long = Constants.Audio.MAX_RECORD_DURATION_MS
    ): ByteArray? {
        val audioRecord = createAudioRecord(Constants.Audio.SAMPLE_RATE_HZ)

        val bufferSize = AudioRecord.getMinBufferSize(
            Constants.Audio.SAMPLE_RATE_HZ,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        val buffer = ByteArray(bufferSize)
        val output = ByteArrayOutputStream()

        val recordStartTime = System.currentTimeMillis()
        try {
            audioRecord.startRecording()
            
            if (audioRecord.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
                Log.e(TAG, "AudioRecord failed to start")
                return null
            }

            while (isRecordingAudio) {
                val bytesRead = audioRecord.read(buffer, 0, bufferSize)

                if (bytesRead > 0) {
                    output.write(buffer, 0, bytesRead)
                } else if (bytesRead < 0) {
                    Log.e(TAG, "AudioRecord.read() error: $bytesRead")
                    break
                }

                // Stop if max duration reached
                if (System.currentTimeMillis() - recordStartTime > maxRecordDurationMillis) {
                    _isRecordingFlag.set(false)
                    break
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error during recording", e)
        } finally {
            _isRecordingFlag.set(false)
            try {
                audioRecord.stop()
            } catch (_: IllegalStateException) {
                // Ignore if already stopped
            }
            audioRecord.release()
        }

        return output.toByteArray()
    }


    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun createAudioRecord(
        sampleRateInHz: Int = Constants.Audio.SAMPLE_RATE_HZ
    ): AudioRecord {
        val minBufferSize = AudioRecord.getMinBufferSize(
            sampleRateInHz,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val bufferSize = maxOf(
            minBufferSize * Constants.Audio.BUFFER_SIZE_MULTIPLIER,
            Constants.Audio.MIN_BUFFER_SIZE
        )
        
        // Try VOICE_RECOGNITION first (better for speech on TV), fallback to MIC
        val audioSources = listOf(
            MediaRecorder.AudioSource.VOICE_RECOGNITION,
            MediaRecorder.AudioSource.MIC,
            MediaRecorder.AudioSource.DEFAULT
        )
        
        for (source in audioSources) {
            try {
                val audioRecord = AudioRecord(
                    source,
                    sampleRateInHz,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize
                )
                if (audioRecord.state == AudioRecord.STATE_INITIALIZED) {
                    return audioRecord
                } else {
                    audioRecord.release()
                }
            } catch (_: Exception) {
                // Try next source
            }
        }
        
        // Last resort fallback
        return AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRateInHz,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )
    }


}

data class VocalAssistantUIState(
    val isRecording: Boolean = false,
    val error: String? = null
)