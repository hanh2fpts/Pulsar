plugins {
    alias(libs.plugins.pulsar.android.feature)
}

android {
    namespace = "com.hmst.pulsar.feature.moneyflow"
}

dependencies {
    implementation(projects.core.network)
}
