plugins {
    alias(libs.plugins.pulsar.android.feature)
}

android {
    namespace = "com.hmst.pulsar.feature.priceboard"
}

dependencies {
    implementation(projects.core.network)
}
