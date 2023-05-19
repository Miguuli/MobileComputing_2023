plugins {
    id("com.android.library")
    id("com.google.dagger.hilt.android")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.example.core.designsystem"
    compileSdk = sdk.compile

    defaultConfig {
        minSdk = sdk.min
        targetSdk = sdk.target

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(androidx.core.ktx)
    implementation(kotlinx.coroutines.android.android)
    implementation(androidx.compose.ui.ui)
    implementation(androidx.compose.material3)
    implementation(androidx.compose.foundation.foundation)
    implementation(androidx.compose.foundation.foundation_layout)
//    implementation("androidx.compose.foundation:foundation")
  //  implementation("androidx.compose.foundation:foundation-layout")
    /*
    implementation(androidx.compose.material)
    implementation(androidx.compose.material_icons)
    implementation(androidx.compose.ui.preview)
    implementation(androidx.lifecycle.compose)
    implementation(androidx.navigation.compose)
    implementation(androidx.lifecycle.runtime)
    implementation(androidx.constraintlayout.compose)
    implementation(androidx.navigation.hilt.compose)
     */
    //implementation("androidx.compose.ui:ui")
    //implementation("androidx.compose.ui:ui-graphics")
    // Hilt for DI
    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation(platform("androidx.compose:compose-bom:2023.04.01"))

    //implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(project(mapOf("path" to ":core-model")))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.04.01"))
    implementation("com.jakewharton.timber:timber:5.0.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
kotlin{
    jvmToolchain(17)
}
kapt {
    correctErrorTypes = true
}