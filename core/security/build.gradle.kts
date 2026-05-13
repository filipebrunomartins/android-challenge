plugins {
    alias(libs.plugins.movieflux.android.library)
}

android {
    namespace = "com.challenge.movieflux.core.security"
}

dependencies {
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.security.crypto)
}