plugins {
    alias(libs.plugins.movieflux.android.feature.impl)
    alias(libs.plugins.movieflux.android.feature.api)
    alias(libs.plugins.movieflux.android.library.compose)
    alias(libs.plugins.movieflux.android.library.jacoco)
}

android {
    namespace = "com.challenge.movieflux.favorites"
}

dependencies {
    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    implementation(libs.androidx.navigation.compose)
}