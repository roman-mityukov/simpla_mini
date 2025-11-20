plugins {
    alias(libs.plugins.simpla.android.library)
    alias(libs.plugins.simpla.hilt)
}
android {
    namespace = "io.mityukov.simpla.core.data"
}
dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:log"))
    implementation(project(":core:domain"))
    implementation(project(":core:network-retrofit"))
    implementation(libs.androidx.lifecycle.service)
}