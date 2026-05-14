plugins {
    alias(libs.plugins.movieflux.android.library)
    alias(libs.plugins.movieflux.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.challenge.movieflux.core.security"
}

dependencies {
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.biometric.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.security.crypto)
}