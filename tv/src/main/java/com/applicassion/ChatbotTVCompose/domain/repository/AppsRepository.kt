package com.applicassion.ChatbotTVCompose.domain.repository

import com.applicassion.ChatbotTVCompose.domain.model.AppModel

interface AppsRepository {
    /**
     * Get all installed apps that can be launched
     */
    fun getInstalledApps(): List<AppModel>
    
    /**
     * Launch an app by package name
     * @return true if launched successfully
     */
    fun launchApp(packageName: String): Boolean
}

