// settings.gradle.kts

pluginManagement {
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
        google()       // << THÊM DÒNG NÀY VÀO ĐÂY
        mavenCentral()
        maven { url = uri("https.jitpack.io") }
    }
}

rootProject.name = "QuanLyChiTieu" // Tên project của bạn
include(":app")