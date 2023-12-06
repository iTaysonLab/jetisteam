@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidPluginApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "bruhcollective.itaysonlab.jetisteam"
    compileSdk = 34

    defaultConfig {
        applicationId = "bruhcollective.itaysonlab.jetisteam"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
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
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/commonMain/**"
            excludes += "/META-INF/kotlin-project-structure-metadata.json"
        }
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.5.4")
    implementation("me.onebone:toolbar-compose:2.3.5")

    implementation(project(":core"))

    implementation(libs.androidKtxCore)
    implementation(libs.androidKtxActivity)
    implementation(libs.androidKtxActivityCompose)
    implementation(libs.bundles.androidLifecycle)

    implementation(libs.bundles.compose)
    implementation(libs.bundles.ksteam)

    implementation(libs.composeMaterialIcons)
    implementation(libs.composeMotionCore)
    implementation(libs.accompanistSystemUi)

    implementation(libs.decompose)
    implementation(libs.decomposeExtensionCompose)
    implementation(libs.kotlinxCollections)

    implementation(libs.koin)
    implementation(libs.koinAndroid)
    implementation(libs.coilCompose)

    debugImplementation(libs.composeUiTooling)
}