plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "1.9.21"
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")


}

android {
    namespace = "com.example.maplecinema"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.maplecinema"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    signingConfigs {
        create("release") {
            storeFile = file("../my-release-key.jks")  // Đường dẫn đến file keystore
            storePassword = "18012002"
            keyAlias = "my-key-alias"
            keyPassword = "18012002"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Dependency Injection (Hilt)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.runner)
    implementation(libs.androidx.espresso.core)
    kapt(libs.hilt.compiler)

    // Kotlin Serialization JSON library
    implementation(libs.kotlinx.serialization.json)

    // Retrofit with Kotlin Serialization converter
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    // Networking (Retrofit & OkHttp)
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)

    // Video Playback (ExoPlayer)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.hls)
    implementation(libs.androidx.media3.ui)

    // Play video from YouTube URL
    implementation(libs.core)

    // Image Loading (Coil)
    implementation(libs.coil.compose)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // Paging
    implementation(libs.androidx.paging.compose)

    // ConstraintLayout for Compose
    implementation(libs.androidx.constraintlayout.compose)

    // System UI Controller (Accompanist)
    implementation(libs.accompanist.systemuicontroller)
    //Skelton UI
    implementation (libs.compose.shimmer)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
kapt {
    correctErrorTypes = true
}