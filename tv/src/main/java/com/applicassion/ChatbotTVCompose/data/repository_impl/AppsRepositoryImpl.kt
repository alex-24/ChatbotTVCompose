package com.applicassion.ChatbotTVCompose.data.repository_impl

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.applicassion.ChatbotTVCompose.domain.model.AppModel
import com.applicassion.ChatbotTVCompose.domain.repository.AppsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppsRepository {

    companion object {
        private const val TAG = "AppsRepositoryImpl"
    }

    // Cache to avoid repeated expensive queries
    private var cachedApps: List<AppModel>? = null
    private val cacheMutex = Mutex()

    override suspend fun getInstalledApps(): List<AppModel> = withContext(Dispatchers.IO) {
        // Return cached if available
        cacheMutex.withLock {
            cachedApps?.let { return@withContext it }
        }

        val startTime = System.currentTimeMillis()
        val packageManager = context.packageManager

        // Get TV apps (Leanback launcher category)
        val tvApps = getTvApps(packageManager)

        // If no TV apps found, fall back to regular launcher apps
        val resolveInfos = tvApps.ifEmpty { getRegularApps(packageManager) }

        // Filter and get unique package names first (fast)
        val uniquePackages = resolveInfos
            .filter { it.activityInfo.packageName != context.packageName }
            .distinctBy { it.activityInfo.packageName }

        // Load icons in parallel using coroutines
        val apps = coroutineScope {
            uniquePackages.map { resolveInfo ->
                async(Dispatchers.IO) {
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
                        Log.e(TAG, "Failed to load app: ${resolveInfo.activityInfo.packageName}", e)
                        null
                    }
                }
            }.awaitAll().filterNotNull()
        }

        val sortedApps = apps.sortedBy { it.label.lowercase() }

        // Cache the result
        cacheMutex.withLock {
            cachedApps = sortedApps
        }

        Log.d(TAG, "Loaded ${sortedApps.size} apps in ${System.currentTimeMillis() - startTime}ms")
        sortedApps
    }

    private fun getTvApps(packageManager: PackageManager) =
        packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
            },
            0
        )

    private fun getRegularApps(packageManager: PackageManager) =
        packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            },
            0
        )

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
            Log.e(TAG, "Failed to launch app: $packageName", e)
            false
        }
    }

    /**
     * Clear the cache to force a refresh on next load
     */
    suspend fun invalidateCache() {
        cacheMutex.withLock {
            cachedApps = null
        }
    }
}
