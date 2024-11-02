// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    //Dagger hilt
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.jetbrainsKotlinSerialization) apply false
}