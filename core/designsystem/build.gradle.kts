plugins {
    alias(libs.plugins.simpla.android.library)
    alias(libs.plugins.simpla.android.library.compose)
}
android {
    namespace = "io.mityukov.simpla.core.designsystem"
}

dependencies {
    api(libs.androidx.material3)
    api(libs.androidx.compose.material.iconsExtended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}