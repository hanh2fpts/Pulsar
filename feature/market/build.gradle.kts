plugins {
    alias(libs.plugins.pulsar.android.feature)
}

android {
    namespace = "com.hmst.pulsar.feature.market"
}

dependencies {
    implementation(projects.core.network)
}
