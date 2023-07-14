import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.libs

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidPluginLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinParcelize)
}

android {
    namespace = "bruhcollective.itaysonlab.cobalt.core.ksteam"
    compileSdk = 33

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(project(":core:decomposekit"))
    implementation(libs.coroutines)
    implementation(libs.decompose)
    implementation(libs.koin)
    implementation(libs.koinAndroid)
    implementation(libs.mmkv)
    implementation(libs.ktorClientEngineOkHttp)
    implementation(libs.bundles.ksteam)
}