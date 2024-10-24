@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidPluginApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.composeCompiler)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    // id("com.vk.vkompose") version "0.4.2"
}

/*vkompose {
    recompose {
        isHighlighterEnabled = true
        isLoggerEnabled = true
    }
}*/

android {
    namespace = "bruhcollective.itaysonlab.cobalt"
    compileSdk = 35

    defaultConfig {
        applicationId = "bruhcollective.itaysonlab.cobalt"
        minSdk = 21
        targetSdk = 35

        versionCode = 9
        versionName = "alpha 9"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            applicationIdSuffix = ".release"

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/commonMain/**"
            excludes += "/META-INF/kotlin-project-structure-metadata.json"
            excludes += "/META-INF/versions/9/previous-compilation-data.bin"
        }
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-crashlytics")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.5.4")
    implementation("me.onebone:toolbar-compose:2.3.5")

    implementation(project(":core"))

    implementation(libs.androidKtxCore)
    implementation(libs.androidKtxActivity)
    implementation(libs.androidKtxActivityCompose)
    implementation(libs.pagingRuntime)
    implementation(libs.pagingCompose)
    implementation(libs.bundles.androidLifecycle)
    implementation(libs.kotlinxDateTime)

    implementation(libs.bundles.compose)
    implementation(libs.bundles.ksteam)
    implementation(libs.bundles.camerax)
    implementation(libs.mlkit.barcodescanning)

    implementation(libs.composeMaterialIcons)
    implementation(libs.composeMotionCore)
    implementation(libs.accompanistSystemUi)
    implementation(libs.accompanistPermissions)

    implementation(libs.decompose)
    implementation(libs.decomposeExtensionCompose)
    implementation(libs.decomposeExtensionComposeExperimental)
    implementation(libs.kotlinxCollections)

    implementation(libs.koin)
    implementation(libs.koinAndroid)
    implementation(libs.coilCompose)

    debugImplementation(libs.composeUiTooling)
}