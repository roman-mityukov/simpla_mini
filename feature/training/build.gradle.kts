plugins {
    alias(libs.plugins.simpla.android.feature)
    alias(libs.plugins.simpla.android.library.compose)
    alias(libs.plugins.simpla.hilt)
}

android {
    namespace = "io.mityukov.simpla.training"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:log"))
    implementation(project(":core:domain"))
    implementation(project(":core:yandexmap"))
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}