// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrainsKotlinKapt) apply false
    alias(libs.plugins.ksp.devtool)
    alias(libs.plugins.daggerHiltAndroid) apply false
    alias(libs.plugins.kotlin.compose) apply false
}