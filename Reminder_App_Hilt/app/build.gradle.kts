
plugins {
    id("com.android.application")
    id("com.google.dagger.hilt.android")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = sdk.compile

    defaultConfig {
        applicationId = "com.example.myapplication"
        
        minSdk = sdk.min
        targetSdk = sdk.target
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.example.myapplication"
}

dependencies {
    implementation(project(":core-model"))
    implementation(project(":core-domain"))
    implementation(androidx.core.ktx)
    implementation(androidx.compose.ui.ui)
    //implementation(androidx.compose.material)
    //implementation(androidx.compose.material_icons)
    //implementation(androidx.compose.ui.preview)
    implementation(androidx.lifecycle.compose)
    implementation(androidx.navigation.compose)
    implementation(androidx.lifecycle.runtime)
    implementation(androidx.constraintlayout.compose)

    implementation(androidx.navigation.hilt.compose)

    // Hilt for DI
    implementation("com.google.dagger:hilt-android:2.46.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation(platform("androidx.compose:compose-bom:2023.05.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    //implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.05.01"))
    kapt("com.google.dagger:hilt-android-compiler:2.46.1")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.3")
}

kapt {
    correctErrorTypes = true
}

kotlin{
    jvmToolchain(17)
}