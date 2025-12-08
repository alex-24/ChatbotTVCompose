package com.applicassion.ChatbotTVCompose.domain.model

import android.content.Intent
import android.graphics.drawable.Drawable

/**
 * Domain model representing an installed app on the device
 */
data class AppModel(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val launchIntent: Intent?
)

