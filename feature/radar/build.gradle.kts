plugins {
    alias(libs.plugins.pulsar.android.feature)
}

android {
    namespace = "com.hmst.pulsar.feature.radar"
}

dependencies {
    implementation(projects.core.network)
    implementation(libs.androidx.compose.material.icons.extended)
}
