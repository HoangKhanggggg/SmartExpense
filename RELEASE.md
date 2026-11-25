# Đóng gói bản Release (APK / AAB) có chữ ký số

Bản release bật **R8** (thu gọn + làm rối mã) và **shrinkResources**, ký bằng **Keystore** của nhóm.

## 1. Tạo Keystore (làm 1 lần, nhóm trưởng giữ file)

Mở Terminal tại thư mục gốc dự án (Windows PowerShell hoặc Terminal của Android Studio):

```
keytool -genkeypair -v -keystore smartexpense-release.jks -alias smartexpense -keyalg RSA -keysize 2048 -validity 10000
```

Nhập mật khẩu và thông tin được hỏi. Hoặc dùng Android Studio: *Build > Generate Signed App Bundle / APK > Create new...*.

**Quan trọng**: sao lưu file `.jks` và mật khẩu ở nơi an toàn. Mất keystore thì không cập nhật được app đã phát hành.
File `.jks` và `keystore.properties` **không được commit** (đã có trong `.gitignore`).

## 2. Khai báo keystore cho Gradle

Sao chép `keystore.properties.example` thành `keystore.properties` (cùng cấp `gradlew`) và điền đúng 4 dòng:
`storeFile`, `storePassword`, `keyAlias`, `keyPassword`.

## 3. Build

```
./gradlew assembleRelease     # APK  -> app/build/outputs/apk/release/app-release.apk
./gradlew bundleRelease       # AAB  -> app/build/outputs/bundle/release/app-release.aab
```

(Windows PowerShell: dùng `.\gradlew.bat` thay cho `./gradlew`.)

Không có `keystore.properties`: build vẫn chạy nhưng APK ký bằng khóa **debug**, chỉ dùng để thử.
Bài nộp phải dùng APK/AAB ký bằng keystore của nhóm.

## 4. Kiểm tra trước khi nộp

1. Cài APK lên **thiết bị thật** (hoặc `adb install -r app/build/outputs/apk/release/app-release.apk`) và thử:
   đăng nhập, thêm giao dịch, thống kê, tỷ giá, đổi ngôn ngữ, sao lưu/khôi phục.
2. Xem chữ ký: `apksigner verify --print-certs app-release.apk` (nằm trong `build-tools` của Android SDK).
3. Nếu app crash chỉ ở bản release (debug chạy bình thường), nguyên nhân thường là R8 xóa/đổi tên class cần reflection:
   thêm `-keep` vào `app/proguard-rules.pro`, build lại. File `app/build/outputs/mapping/release/mapping.txt` giúp đọc stack trace.
4. File `google-services.json` thật phải có trong `app/` khi build thì Firebase mới đăng nhập được (file này không nằm trong Git).

## 5. Nộp bài

- Moodle: APK/AAB release + file `.zip` mã nguồn (Build > Clean Project trước khi nén; không kèm `.jks` và mật khẩu).
- Link GitHub công khai của nhóm.
