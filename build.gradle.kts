// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidPluginApplication).apply(false)
    alias(libs.plugins.androidPluginLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinParcelize).apply(false)
    alias(libs.plugins.darwinParcelize).apply(false)
    alias(libs.plugins.kotlinSerialization).apply(false)
}
true // Needed to make the Suppress annotation work for the plugins block