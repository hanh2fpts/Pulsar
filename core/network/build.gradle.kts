import java.util.Properties

plugins {
    alias(libs.plugins.pulsar.android.library)
    alias(libs.plugins.pulsar.hilt)
    alias(libs.plugins.kotlin.serialization)
}

// DNSE credentials come from the git-ignored local.properties
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

android {
    namespace = "com.hmst.pulsar.core.network"

    defaultConfig {
        buildConfigField("String", "DNSE_API_KEY",
            "\"${localProps.getProperty("DNSE_API_KEY", "")}\"")
        buildConfigField("String", "DNSE_API_SECRET",
            "\"${localProps.getProperty("DNSE_API_SECRET", "")}\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // api: features that call an API need Retrofit and the serialization annotations
    api(libs.retrofit)
    api(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.msgpack.core)
    implementation(libs.okhttp)
    implementation(libs.androidx.lifecycle.viewmodel)
}
