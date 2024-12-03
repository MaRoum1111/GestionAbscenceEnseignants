// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false  // Alias pour le plugin Android
}

buildscript {
    repositories {
        google()          // Dépôt Google pour les plugins Android et Firebase
        mavenCentral()    // Dépôt Maven Central pour les autres dépendances
    }

    dependencies {
        classpath(libs.gradle)                  // Plugin Gradle pour Android
        classpath(libs.google.services)         // Plugin Google Services (nécessaire pour Firebase)
        classpath(libs.firebase.crashlytics.gradle) // Crashlytics pour Firebase
    }
}
