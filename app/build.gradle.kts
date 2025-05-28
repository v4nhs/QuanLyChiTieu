plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Chỉ cần một dòng để áp dụng plugin kapt được:
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.quanlychitieu"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.quanlychitieu"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Các dependency cơ bản
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Dependency của Room
    implementation(libs.androidx.room.common.jvm)
    // Chọn sử dụng một trong hai Room runtime nếu cần (đảm bảo alias trong version catalog đã được định nghĩa chính xác):
    implementation(libs.androidx.room.runtime.android)
    // implementation(libs.androidx.room.runtime)  // Nếu alias này dùng để chỉ runtime cho các nền tảng khác, có thể loại bỏ nếu không cần.

    // Dependency cho ViewModel KTX, cung cấp viewModelScope:
    implementation(libs.lifecycleviewmodelktx)

    // Dependency dành cho coroutines (nếu đã định nghĩa alias trong version catalog):
    implementation(libs.kotlinxcoroutinesandroid)

    // Room compiler (dùng để xử lý annotation, bắt buộc với Room):
    kapt(libs.androidx.room.compiler)

    // Các dependency test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
