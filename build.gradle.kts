// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false  // Alias pour le plugin Android
}

buildscript {
    repositories {
        google()  // Dépôt Google pour les plugins Android et Firebase
        mavenCentral()  // Dépôt Maven Central pour les autres dépendances
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.0.2")  // Plugin Gradle pour Android
        classpath("com.google.gms:google-services:4.3.8")  // Plugin Google Services (nécessaire pour Firebase)
    }
}
