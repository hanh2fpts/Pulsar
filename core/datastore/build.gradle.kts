plugins {
    alias(libs.plugins.pulsar.android.library)
    alias(libs.plugins.pulsar.hilt)
}

android {
    namespace = "com.hmst.pulsar.core.datastore"
}

dependencies {
    // api: features read and write their own keys on the shared DataStore
    api(libs.androidx.datastore.preferences)
}
