buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-serialization:${libs.versions.kotlin}")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    kotlin("plugin.serialization") version "2.0.0" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.23" apply false
}
