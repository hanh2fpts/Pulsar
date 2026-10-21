plugins {
    alias(libs.plugins.pulsar.android.feature)
}

android {
    namespace = "com.hmst.pulsar.feature.portfolio"
}

dependencies {
    implementation(projects.core.network)
}
