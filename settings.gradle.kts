pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "SimplaMini"
include(":app")
include(":core:log")
include(":core:common")
include(":core:domain")
include(":core:network-retrofit")
include(":core:yandexmap")
include(":core:designsystem")
include(":feature:training-list")
include(":feature:training")
include(":core:data")
