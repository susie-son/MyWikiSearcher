buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-serialization:${libs.versions.kotlin}")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    kotlin("plugin.serialization") version "2.0.0" apply false
}
