# Hiệu năng và rò rỉ bộ nhớ (Cột mốc 5)

Tài liệu này hướng dẫn kiểm tra **rò rỉ bộ nhớ** bằng LeakCanary và đo **độ mượt khi cuộn danh sách**
bằng Android Profiler. Phần "Kết quả đo" do nhóm tự điền bằng số liệu thật trên máy của mình.

## 1. LeakCanary (chỉ bản debug)

- Đã thêm `debugImplementation(libs.leakcanary.android)` (phiên bản trong `gradle/libs.versions.toml`).
  Bản release **không** chứa LeakCanary. Không cần viết thêm code: thư viện tự cài khi app khởi động.
- Cách chạy: Run bản **debug** trên máy ảo/máy thật. Khi có rò rỉ, LeakCanary hiện thông báo
  "Leaks" và có icon *Leaks* trên launcher để xem chuỗi tham chiếu (leak trace).
- Kịch bản thử gợi ý (mỗi bước làm 2–3 lần rồi đợi vài giây):
  1. Chuyển qua lại 4 tab: Trang chủ, Giao dịch, Thống kê, Cá nhân.
  2. Thêm giao dịch, mở Chi tiết giao dịch, bấm Back.
  3. Đổi ngôn ngữ và chế độ sáng/tối (Activity bị tạo lại).
  4. Đăng xuất, đăng nhập lại; (admin) vào Quản trị, Quản lý danh mục.

### Kiểm tra bằng đọc code (đã rà trước khi đo)

- 11/11 Fragment đều gán `_binding = null` trong `onDestroyView()`.
- `AppDatabase` và `AppPrefs` giữ `applicationContext`, không giữ Activity.
- Mọi `.collect` Flow ở tầng giao diện (Fragment/Activity) đều nằm trong `repeatOnLifecycle`, nên tự dừng khi màn hình không hiển thị.
- Glide gắn với vòng đời của View (`Glide.with(view)`), tự hủy yêu cầu khi View bị thu hồi.

Các điểm trên chỉ là kết quả đọc code; kết luận cuối cùng phải dựa vào LeakCanary khi chạy thật.

## 2. Android Profiler: cuộn danh sách có mượt không

1. Android Studio: *View > Tool Windows > Profiler*, chọn tiến trình `com.example.smartexpense`.
2. Tạo nhiều dữ liệu: chế độ DEMO hoặc thêm khoảng 50–100 giao dịch trong một tháng.
3. Mở tab **Giao dịch**, cuộn nhanh lên xuống khoảng 20 giây.
4. Quan sát **CPU** (không có đỉnh kéo dài) và **Memory** (đường bộ nhớ dao động quanh một mức, không tăng mãi).
5. Chụp ảnh màn hình Profiler đưa vào báo cáo.
6. Lặp lại với màn Thêm giao dịch (lưới danh mục có ảnh Glide) khi **bật Chế độ máy bay**: icon vẫn hiện nhờ cache đĩa của Glide
   và icon cục bộ dự phòng.

Gợi ý bật *Developer options > Profile GPU rendering > On screen as bars* để thấy cột khung hình khi cuộn.

## 3. Kết quả đo (nhóm tự điền)

| Màn hình | Thiết bị | Bộ nhớ sau 20 s cuộn | Có rò rỉ (LeakCanary)? | Ghi chú |
|---|---|---|---|---|
| Giao dịch | | | | |
| Thống kê | | | | |
| Thêm giao dịch | | | | |
