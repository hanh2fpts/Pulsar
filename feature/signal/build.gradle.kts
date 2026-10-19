plugins {
    alias(libs.plugins.pulsar.android.feature)
}

android {
    namespace = "com.hmst.pulsar.feature.signal"
}

dependencies {
    implementation(projects.core.network)
}