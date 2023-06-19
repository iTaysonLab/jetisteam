pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

rootProject.name = "Cobalt"

includeBuild("build-extensions")
include(":app")

include(":core:decomposekit")
include(":core:ksteam")
include(":core:featureflags")
include(":feature:home")
include(":feature:news")
include(":feature:signin")
include(":feature:webview")
include(":feature:profile")
