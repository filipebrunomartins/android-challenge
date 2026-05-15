plugins {
    alias(libs.plugins.movieflux.android.library)
    alias(libs.plugins.movieflux.android.library.jacoco)
    alias(libs.plugins.movieflux.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.challenge.movieflux.core.data"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api(projects.core.domain)
    api(projects.core.common)
    api(projects.core.security)
    api(projects.core.network)
//    api(projects.core.database)
//    api(projects.core.datastore)

    implementation(libs.retrofit.gson)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.serialization.json)
}