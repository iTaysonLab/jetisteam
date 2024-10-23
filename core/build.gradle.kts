@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidPluginLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "bruhcollective.itaysonlab.cobalt.core"
    compileSdk = 34

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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

dependencies {
    implementation(libs.pagingRuntime)

    implementation(libs.composeRuntime)
    implementation(libs.coroutines)

    implementation(libs.decompose)
    implementation(libs.essentyLifecycleCoroutines)

    implementation(libs.mvikotlin)
    implementation(libs.mvikotlin.main)
    implementation(libs.mvikotlin.logging)
    implementation(libs.mvikotlin.extensions.coroutines)

    implementation(libs.koin)
    implementation(libs.koinAndroid)

    implementation(libs.kotlinxCollections)
    implementation(libs.kotlinxDateTime)
    implementation(libs.ktorClientEngineOkHttp)
    
    implementation(libs.bundles.ksteam)
    implementation(libs.bundles.ktorClient)
}