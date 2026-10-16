plugins {
    alias(libs.plugins.pulsar.android.library)
    alias(libs.plugins.pulsar.hilt)
    alias(libs.plugins.pulsar.android.room)
}

android {
    namespace = "com.hmst.pulsar.core.database"
}
