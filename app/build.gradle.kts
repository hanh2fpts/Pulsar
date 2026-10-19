plugins {
    alias(libs.plugins.pulsar.android.application)
    alias(libs.plugins.pulsar.hilt)
    // AppNavHost declares @Serializable tab graph routes
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.hmst.pulsar"

    defaultConfig {
        applicationId = "com.hmst.pulsar"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.feature.market)
    implementation(projects.feature.priceboard)
    implementation(projects.feature.signal)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
