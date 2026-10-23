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

    signingConfigs {
        val releaseStoreFile = providers.gradleProperty("PULSAR_STORE_FILE").orNull
        if (releaseStoreFile != null) {
            create("release") {
                storeFile = file(releaseStoreFile)
                storePassword = providers.gradleProperty("PULSAR_STORE_PASSWORD").get()
                keyAlias = providers.gradleProperty("PULSAR_KEY_ALIAS").get()
                keyPassword = providers.gradleProperty("PULSAR_KEY_PASSWORD").get()
            }
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.findByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.network)
    implementation(projects.feature.moneyflow)
    implementation(projects.feature.portfolio)
    implementation(projects.feature.priceboard)
    implementation(projects.feature.radar)
    implementation(projects.feature.signal)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.icons.extended)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
