buildscript {
    dependencies {
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.0")
        classpath ("com.google.gms:google-services:4.3.2")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}