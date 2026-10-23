plugins {
    alias(libs.plugins.pulsar.android.library.compose)
}

android {
    namespace = "com.hmst.pulsar.core.designsystem"
}

dependencies {
    api(libs.vico.compose)
    api(libs.vico.compose.m3)
}
