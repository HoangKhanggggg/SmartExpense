import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
}

// google-services.json chứa khóa API thật nên KHÔNG nằm trong Git (.gitignore).
// Clone repo về chưa có file này -> tự chép bản mẫu (giá trị giả) để build được.
// Đăng nhập Firebase chỉ hoạt động khi bạn đặt file thật tải từ Firebase Console (xem API_SETUP.md).
val googleServicesFile = file("google-services.json")
if (!googleServicesFile.exists()) {
    file("google-services.json.example").copyTo(googleServicesFile)
    logger.lifecycle("Chưa có app/google-services.json -> dùng bản mẫu; Đăng nhập Firebase sẽ không hoạt động (vẫn dùng được chế độ DEMO).")
}

// Đọc admin.emails từ local.properties (không commit) để BuildConfig dùng cho isAdmin()
// Nếu thiếu hoặc rỗng: app hoạt động bình thường nhưng không có tài khoản admin
val localProperties = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
val adminEmails = localProperties.getProperty("admin.emails", "")

android {
    namespace = "com.example.smartexpense"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.smartexpense"
        minSdk = 24
        targetSdk = 36
        // 1.4 (10): dời cấu trúc package chuẩn ui/viewmodel/model/repository/adapter,
        // phân hệ Tỷ giá USD→VND (Retrofit + cache Room + 3 trạng thái Resource)
        versionCode = 10
        versionName = "1.4"
        vectorDrawables { useSupportLibrary = true }
        // ADMIN_EMAILS là chuỗi email phân tách bởi dấu phẩy (vd "admin@example.com")
        buildConfigField("String", "ADMIN_EMAILS", "\"$adminEmails\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true   // UI dùng XML + View Binding, không findViewById
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        // Bắt buộc vì dùng java.time (minSdk 24 < 26)
        isCoreLibraryDesugaringEnabled = true
    }
}

// Cú pháp mới của Kotlin 2.x (kotlinOptions đã deprecated)
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.play.services) // await() cho Task của Firebase

    // Firebase Auth + Firestore (dùng BoM để đồng bộ phiên bản)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Glide
    implementation(libs.glide)

    // EncryptedSharedPreferences
    implementation(libs.androidx.security.crypto)

    // Retrofit: gọi REST API JSON (phân hệ Tỷ giá USD→VND)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // LeakCanary: chỉ bản debug, tự phát hiện rò rỉ bộ nhớ (xem docs/PERFORMANCE.md)
    debugImplementation(libs.leakcanary.android)

    coreLibraryDesugaring(libs.desugar.jdk.libs)
}
