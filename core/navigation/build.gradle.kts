plugins {
    alias(libs.plugins.movieflux.android.library)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.challenge.movieflux.core.navigation"
}

dependencies {
    implementation(libs.androidx.savedstate.compose)
}