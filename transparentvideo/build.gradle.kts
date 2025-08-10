plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.tritus.transparentvideo"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
        targetSdk = 35
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
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("androidx.compose.foundation:foundation:1.8.3")
    implementation("androidx.compose.runtime:runtime:1.8.3")
    implementation("androidx.compose.ui:ui:1.8.3")
    implementation("androidx.lifecycle:lifecycle-common:2.9.2")

    implementation("com.google.android.exoplayer:exoplayer-core:2.19.1")
}
