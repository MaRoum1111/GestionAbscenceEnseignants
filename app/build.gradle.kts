plugins {
    alias(libs.plugins.android.application)  // Plugin Android
    id("com.google.gms.google-services")    // Plugin Google Services pour Firebase
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
    // Import the BoM for the Firebase platform
    implementation (platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation ("com.google.firebase:firebase-auth")       // Firebase Authentification
    implementation ("com.google.firebase:firebase-firestore")    // Firebase Firestore
    implementation ("com.google.firebase:firebase-storage")      // Firebase Storage

    // Android Dependencies
    implementation(libs.appcompat)          // Composants Android
    implementation(libs.material)           // Material Design
    implementation(libs.activity)           // Composant Activity
    implementation(libs.constraintlayout)   // Layout avec ConstraintLayout
    implementation(libs.recyclerview)       // RecyclerView
    implementation(libs.cardview)           // CardView

    // Tests
    testImplementation(libs.junit)               // Tests unitaires
    androidTestImplementation(libs.ext.junit)    // Tests d'interface utilisateur avec JUnit
    androidTestImplementation(libs.espresso.core) // Tests d'interface utilisateur avec Espresso
}
