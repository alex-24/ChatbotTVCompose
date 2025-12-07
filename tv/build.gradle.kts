import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

val envProps = Properties()
file("../.env").takeIf { it.exists() }?.inputStream()?.use(envProps::load)

// Cloudflare AI Workers
val cloudflareAccountId = envProps.getProperty("CLOUDFLARE_ACCOUNT_ID") ?: ""
val cloudllareAuthToken = envProps.getProperty("CLOUDFLARE_AUTH_TOKEN") ?: ""

android {
    namespace = "com.applicassion.ChatbotTVCompose"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.applicassion.ChatbotTVCompose"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        buildFeatures.buildConfig = true
        /// todo: if null - reject the build
        buildConfigField("String", "CLOUDFLARE_ACCOUNT_ID", "\"$cloudflareAccountId\"")
        buildConfigField("String", "CLOUDFLARE_AUTH_TOKEN", "\"$cloudllareAuthToken\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.hilt.android)
    implementation(libs.androidx.ui.graphics)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.androidx.compose.material.icons.extended)


    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}