import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

@OptIn(ExperimentalKotlinGradlePluginApi::class)
fun KotlinMultiplatformExtension.commonSetup() {
    // Enable Kotlin 1.8.20's new default hierarchy
    targetHierarchy.default()

    // Enable Android support and use JVM 1.8 target
    android {
        jvmToolchain(8)

        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    // Enable iOS support
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // Allow @ExperimentalObjCName annotation
    sourceSets.all {
        languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
    }
}

fun KotlinMultiplatformExtension.androidDependencies(
    handler: KotlinDependencyHandler.() -> Unit
) {
   sourceSets["androidMain"].dependencies(handler)
}

fun KotlinMultiplatformExtension.iosDependencies(
    handler: KotlinDependencyHandler.() -> Unit
) {
    sourceSets["iosMain"].dependencies(handler)
}