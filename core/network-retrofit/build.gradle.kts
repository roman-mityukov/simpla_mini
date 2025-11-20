plugins {
    alias(libs.plugins.simpla.android.library)
    alias(libs.plugins.simpla.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "io.mityukov.simpla.network.retrofit"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:log"))
    implementation(libs.kotlinx.serialization.json)
    api(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.retrofit.logging.interceptor)
}