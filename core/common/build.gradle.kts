plugins {
    alias(libs.plugins.movieflux.jvm.library)
    alias(libs.plugins.movieflux.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}