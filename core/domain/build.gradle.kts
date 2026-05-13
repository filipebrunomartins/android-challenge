plugins {
    alias(libs.plugins.movieflux.android.library)
    alias(libs.plugins.movieflux.android.library.jacoco)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.challenge.movieflux.core.domain"
}

dependencies {
//    api(projects.core.model)
    implementation(libs.javax.inject)
}