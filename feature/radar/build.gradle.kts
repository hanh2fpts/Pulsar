plugins {
    alias(libs.plugins.pulsar.android.feature)
}

android {
    namespace = "com.hmst.pulsar.feature.radar"
}

dependencies {
    implementation(projects.core.network)
    // lưu giá trị GD cuối ngày để tính trung bình 20 phiên
    implementation(projects.core.datastore)
    implementation(libs.androidx.compose.material.icons.extended)
    // logo công ty trong nhóm dẫn dắt
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    testImplementation(libs.junit)
}
