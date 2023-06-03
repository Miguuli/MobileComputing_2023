plugins {
    id("com.android.library")
    id("com.google.dagger.hilt.android")
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.core.database"
    compileSdk = sdk.compile

    defaultConfig {
        minSdk = sdk.min

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
}

dependencies {

    implementation(androidx.core.ktx)

    // Room
    implementation(androidx.room.ktx)
    implementation(androidx.room.runtime)
    implementation(project(mapOf("path" to ":core-model")))
    ksp("androidx.room:room-compiler:2.5.1")

    // Coroutines
    implementation(kotlinx.coroutines.android.android)
    implementation(kotlinx.coroutines.core.core)
    // Hilt for DI
    implementation("com.google.dagger:hilt-android:2.46.1")
    kapt("com.google.dagger:hilt-android-compiler:2.46.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

kapt {
    correctErrorTypes = true
}
kotlin{
    jvmToolchain(17)
}