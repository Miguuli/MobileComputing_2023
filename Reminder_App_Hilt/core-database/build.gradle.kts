plugins {
    id("example.android.library")
    id("example.android.library.jacoco")
    id("example.android.hilt")
    id("example.android.room")
}

android {
    defaultConfig {
    }
    namespace = "com.example.core.database"
}

dependencies {
    implementation(project(":core-model"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
}