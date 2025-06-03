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

// THÊM KHỐI NÀY VÀO ĐỂ KHAI BÁO KHO LƯU TRỮ CHO THƯ VIỆN
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Khuyến khích để quản lý tập trung
    repositories {
        google()       // Cần cho thư viện AndroidX như Room
        mavenCentral() // Kho lưu trữ phổ biến
        // Nếu bạn vẫn dùng AnyChart hoặc các thư viện khác từ JitPack, hãy thêm dòng này:
        // maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "QuanLyChiTieu" // Tên project của bạn
include(":app")