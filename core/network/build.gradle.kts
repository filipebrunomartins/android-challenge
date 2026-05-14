import com.android.build.api.variant.BuildConfigField
import java.io.StringReader
import java.util.Properties

plugins {
    alias(libs.plugins.movieflux.android.library)
    alias(libs.plugins.movieflux.android.library.jacoco)
    alias(libs.plugins.movieflux.hilt)
    id("kotlinx-serialization")
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.challenge.movieflux.core.network"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api(projects.core.common)

    implementation(libs.retrofit.gson)
    implementation(libs.gson)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)

    testImplementation(libs.kotlinx.coroutines.test)
}

val backendUrl = providers.fileContents(
    isolated.rootProject.projectDirectory.file("local.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["TMDB_BASE_URL"]
}.orElse("http://example.com")

val tokenApi = providers.fileContents(
    isolated.rootProject.projectDirectory.file("local.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["TMDB_TOKEN"]
}.orElse("")

androidComponents {
    onVariants {
        it.buildConfigFields!!.put("TMDB_BASE_URL", backendUrl.map { value ->
            BuildConfigField(type = "String", value = """"$value"""", comment = null)
        })
        it.buildConfigFields!!.put("TMDB_TOKEN", tokenApi.map { value ->
            BuildConfigField(type = "String", value = """"$value"""", comment = null)
        })
    }
}