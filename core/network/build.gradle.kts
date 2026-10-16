plugins {
    alias(libs.plugins.pulsar.android.library)
    alias(libs.plugins.pulsar.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.hmst.pulsar.core.network"
}

dependencies {
    // api: features that call an API need Retrofit and the serialization annotations
    api(libs.retrofit)
    api(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.logging)
}
