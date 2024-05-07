import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project

fun Project.androidLibrary(moduleNamespace: String) {
    extensions.configure<LibraryExtension>("android") {
        namespace = moduleNamespace
        compileSdk = 34

        defaultConfig {
            minSdk = 21
        }
    }
}