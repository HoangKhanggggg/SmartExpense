# Rules R8/Proguard cho bản release (isMinifyEnabled = true)
# Nếu build release báo lỗi "Missing class ...", mở
# app/build/outputs/mapping/release/missing_rules.txt và thêm các dòng -dontwarn tương ứng.

# Giữ số dòng để đọc stack trace (kèm file mapping.txt)
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Giữ thông tin generic/annotation cho Retrofit + Gson
-keepattributes Signature,InnerClasses,EnclosingMethod,*Annotation*

# Retrofit service + model JSON (Gson dùng reflection nên không được đổi tên field)
-keep class com.example.smartexpense.data.remote.** { *; }
-keep class com.example.smartexpense.model.** { *; }

# Room/Firestore đọc tên field của model; ViewModel tạo bằng reflection
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.lifecycle.ViewModel { <init>(...); }

# EncryptedSharedPreferences (Tink) và các thư viện tham chiếu annotation lúc build
-dontwarn com.google.errorprone.annotations.**
-dontwarn javax.annotation.**
-dontwarn org.checkerframework.**
