plugins {
    alias(libs.plugins.android.application) // Use the alias for android.application
    id("com.google.gms.google-services") // Apply Google Services plugin
}

android {
    namespace = "com.mobdeve.s21.grp4.mco_taftbites"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mobdeve.s21.grp4.mco_taftbites"
        minSdk = 24
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
        // Use Java 17 toolchain
        sourceCompatibility = JavaVersion.VERSION_1_8  // Gradle still needs to know this for backward compatibility
        targetCompatibility = JavaVersion.VERSION_1_8
    }

//    java {
//        toolchain {
//            languageVersion = JavaLanguageVersion.of(17) // Set to Java 17 using the toolchain
//        }
   // }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)
    implementation("com.facebook.android:facebook-android-sdk:16.1.2") // Facebook SDK

    implementation("com.squareup.picasso:picasso:2.8") // Add Picasso
    implementation("com.squareup.picasso:picasso:2.71828")

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.6.0")) // Firebase BOM
    implementation("com.google.firebase:firebase-database") // Firebase Realtime Database
    implementation("com.google.firebase:firebase-firestore")
    implementation(libs.firebase.auth) // Firebase Firestore
    implementation ("com.google.android.gms:play-services-maps:17.0.0")
    implementation ("com.google.firebase:firebase-firestore:24.0.0")

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
