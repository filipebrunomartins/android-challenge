plugins {
    alias(libs.plugins.movieflux.android.library)
    alias(libs.plugins.movieflux.android.library.jacoco)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.challenge.movieflux.core.domain"
}

//todo rever
dependencies {
    api(projects.core.common)
    api(projects.core.model)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)

    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
}