plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.smartfit"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.smartfit"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    // ✅ Import the Firebase BoM (manages versions automatically)
    implementation(platform(libs.firebase.bom))

// 🔐 Authentication
    implementation(libs.firebase.auth.ktx)

// 💾 Firestore Database
    implementation(libs.firebase.firestore.ktx)

// 📦 Realtime Database (if you might use it)
    implementation(libs.firebase.database.ktx)

// ☁️ Cloud Storage
    implementation(libs.firebase.storage.ktx)

// 🔔 Cloud Messaging (Push Notifications)
    implementation(libs.firebase.messaging.ktx)

// 📈 Analytics (optional but helpful)
    implementation(libs.firebase.analytics.ktx)

// 🧠 Remote Config (optional)
    implementation(libs.firebase.config.ktx)

// 🧩 Crashlytics (optional, for crash reports)
    implementation(libs.firebase.crashlytics.ktx)

// 🧠 Performance Monitoring (optional)
    implementation(libs.firebase.perf.ktx)

// 🔑 App Check (optional, for security)
    implementation(libs.firebase.appcheck.ktx)

// 🧭 Dynamic Links (optional)
    implementation(libs.firebase.dynamic.links.ktx)

// 🔥 Installations API (used internally, but okay to add)
    implementation(libs.firebase.installations.ktx)

}