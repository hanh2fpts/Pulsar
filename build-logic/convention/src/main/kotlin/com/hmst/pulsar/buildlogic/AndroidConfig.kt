package com.hmst.pulsar.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** SDK and Java levels shared by every Android module */
internal object AndroidConfig {
    const val COMPILE_SDK = 37
    const val COMPILE_SDK_MINOR = 0
    const val MIN_SDK = 24
    const val TARGET_SDK = 36
    val JAVA_VERSION = JavaVersion.VERSION_11
}

internal fun ApplicationExtension.configureAndroid() {
    compileSdk {
        version = release(AndroidConfig.COMPILE_SDK) {
            minorApiLevel = AndroidConfig.COMPILE_SDK_MINOR
        }
    }
    defaultConfig {
        minSdk = AndroidConfig.MIN_SDK
        targetSdk = AndroidConfig.TARGET_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = AndroidConfig.JAVA_VERSION
        targetCompatibility = AndroidConfig.JAVA_VERSION
    }
}

internal fun LibraryExtension.configureAndroid() {
    compileSdk {
        version = release(AndroidConfig.COMPILE_SDK) {
            minorApiLevel = AndroidConfig.COMPILE_SDK_MINOR
        }
    }
    defaultConfig {
        minSdk = AndroidConfig.MIN_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = AndroidConfig.JAVA_VERSION
        targetCompatibility = AndroidConfig.JAVA_VERSION
    }
}

/** Compose BOM + the Compose libraries every UI module uses */
internal fun Project.addComposeDependencies() {
    dependencies {
        val bom = platform(libs.library("androidx-compose-bom"))
        add("implementation", bom)
        add("implementation", libs.library("androidx-compose-ui"))
        add("implementation", libs.library("androidx-compose-ui-graphics"))
        add("implementation", libs.library("androidx-compose-ui-tooling-preview"))
        add("implementation", libs.library("androidx-compose-material3"))
        add("implementation", libs.library("androidx-compose-material-icons-core"))
        add("debugImplementation", libs.library("androidx-compose-ui-tooling"))
        add("androidTestImplementation", bom)
    }
}
