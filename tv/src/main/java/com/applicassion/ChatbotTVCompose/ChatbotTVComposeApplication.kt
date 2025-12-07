package com.applicassion.ChatbotTVCompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChatbotTVComposeApplication: Application() {
    companion object {
        private const val TAG: String = "ChatbotTVComposeApplication"
    }
}