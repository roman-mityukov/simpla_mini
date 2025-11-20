plugins {
    alias(libs.plugins.simpla.android.library)
}
android {
    namespace = "io.mityukov.simpla.core.log"
}
dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.timber)
    implementation(libs.treessence)
}
