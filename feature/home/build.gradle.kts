plugins {
    alias(libs.plugins.movieflux.android.feature.impl)
    alias(libs.plugins.movieflux.android.feature.api)
    alias(libs.plugins.movieflux.android.library.compose)
    alias(libs.plugins.movieflux.android.library.jacoco)
}

android {
    namespace = "com.challenge.movieflux.feature.home"
}

dependencies {
    implementation(projects.core.data)

    implementation(libs.coil.kt.compose)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(libs.hilt.android.testing)
    debugImplementation(libs.androidx.compose.ui.testManifest)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
}