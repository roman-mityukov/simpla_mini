plugins {
    alias(libs.plugins.simpla.android.library)
}

android {
    namespace = "io.mityukov.geo.tracking.core.yandexmap"
}

dependencies {
    implementation(project(":core:domain"))
    api(libs.yandex.map.kit)
}