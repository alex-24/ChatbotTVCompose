package com.applicassion.ChatbotTVCompose.domain.model

import android.content.Intent
import android.graphics.Bitmap

/**
 * Domain model representing an installed app on the device
 */
data class AppModel(
    val packageName: String,
    val label: String,
    val icon: Bitmap?,
    val launchIntent: Intent?
)
