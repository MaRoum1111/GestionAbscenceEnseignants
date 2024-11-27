plugins {
    alias(libs.plugins.android.application)  // Plugin Android
    id("com.google.gms.google-services")  // Plugin Google Services pour Firebase
}

android {
    namespace = "com.example.gestionabscenceenseignants"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gestionabscenceenseignants"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Firebase Dependencies
    implementation("com.google.firebase:firebase-auth:21.2.0")  // Authentification Firebase
    implementation("com.google.firebase:firebase-firestore:24.8.1")  // Firestore Firebase
// Android Dependencies
    implementation(libs.appcompat)  // AppCompat pour les composants Android de base
    implementation(libs.material)  // Material Design
    implementation(libs.activity)  // Composant Activity
    implementation(libs.constraintlayout)  // Layout avec ConstraintLayout
    implementation(libs.recyclerview)

// Tests
    testImplementation(libs.junit)  // Tests unitaires
    androidTestImplementation(libs.ext.junit)  // Tests d'interface utilisateur avec JUnit
    androidTestImplementation(libs.espresso.core)  // Tests d'interface utilisateur avec Espresso
}
