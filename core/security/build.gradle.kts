plugins {
    alias(libs.plugins.movieflux.android.library)
    alias(libs.plugins.movieflux.android.library.compose)
    alias(libs.plugins.movieflux.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.challenge.movieflux.core.security"

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.biometric.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.security.crypto)

    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
}