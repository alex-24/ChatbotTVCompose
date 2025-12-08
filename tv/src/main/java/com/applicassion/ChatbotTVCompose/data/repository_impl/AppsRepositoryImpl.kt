package com.applicassion.ChatbotTVCompose.data.repository_impl

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.applicassion.ChatbotTVCompose.domain.model.AppModel
import com.applicassion.ChatbotTVCompose.domain.repository.AppsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppsRepository {

    override fun getInstalledApps(): List<AppModel> {
        val packageManager = context.packageManager
        
        // Get TV apps (Leanback launcher category)
        val tvApps = getTvApps(packageManager)
        
        // If no TV apps found, fall back to regular launcher apps
        val apps = if (tvApps.isNotEmpty()) tvApps else getRegularApps(packageManager)
        
        return apps
            .filter { it.packageName != context.packageName } // Exclude ourselves
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase() }
    }

    private fun getTvApps(packageManager: PackageManager): List<AppModel> {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        }
        return resolveApps(packageManager, intent)
    }

    private fun getRegularApps(packageManager: PackageManager): List<AppModel> {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        return resolveApps(packageManager, intent)
    }

    private fun resolveApps(packageManager: PackageManager, intent: Intent): List<AppModel> {
        val resolveInfos = packageManager.queryIntentActivities(intent, 0)
        
        return resolveInfos.mapNotNull { resolveInfo ->
            try {
                AppModel(
                    packageName = resolveInfo.activityInfo.packageName,
                    label = resolveInfo.loadLabel(packageManager).toString(),
                    icon = resolveInfo.loadIcon(packageManager),
                    launchIntent = packageManager.getLaunchIntentForPackage(
                        resolveInfo.activityInfo.packageName
                    )
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun launchApp(packageName: String): Boolean {
        return try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}

