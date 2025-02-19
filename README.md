 Ứng dụng Xem Phim 📱
🚀 Ứng dụng xem phim hiện đại được xây dựng bằng Jetpack Compose, hỗ trợ phát video từ YouTube và nhiều nguồn khác.

📌 Tính năng chính
✅ Danh sách phim theo thể loại, xu hướng.
✅ Tìm kiếm phim, xem thông tin chi tiết.
✅ Phát video từ YouTube với YouTubePlayerView.
✅ Phát video từ nguồn khác với ExoPlayer.
✅ Lưu phim yêu thích vào Room Database.
✅ Giao diện đẹp, hỗ trợ Skeleton UI (Compose Shimmer).

🛠 Công nghệ sử dụng
Jetpack Compose - Xây dựng UI hiện đại, dễ bảo trì.
Hilt (Dagger-Hilt) - Quản lý Dependency Injection.
Retrofit + Kotlin Serialization - Gọi API nhanh chóng.
YouTubePlayerView - Nhúng YouTube Player.
ExoPlayer - Hỗ trợ phát video từ nhiều nguồn.
Room Database - Lưu trữ dữ liệu cục bộ.
Paging 3 - Load danh sách phim hiệu quả.
Coil - Tải ảnh nhanh và tối ưu bộ nhớ.
📸 Ảnh màn hình (Screenshots)
![Screenshot_2025-02-19-12-22-17-033_com example maplecinema](https://github.com/user-attachments/assets/7b548726-eb57-4333-829a-26e7d716d7ba)![Screenshot_2025-02-19-12-21-52-729_com example maplecinema](https://github.com/user-attachments/assets/c06fea3e-6445-487e-972b-08db15f9e904)![Screenshot_2025-02-19-12-21-56-593_com example maplecinema](https://github.com/user-attachments/assets/98d5669b-8313-47bf-89b7-0cd95dc000a0)
![Screenshot_2025-02-19-12-19-46-012_com example maplecinema](https://github.com/user-attachments/assets/9403266c-86ae-41e3-9cc5-76052d5d7133)![Screenshot_2025-02-19-12-19-38-619_com example maplecinema](https://github.com/user-attachments/assets/9ba01204-690c-4c2e-ba25-8040a31beef2)![Screenshot_2025-02-19-12-19-24-351_com example maplecinema](https://github.com/user-attachments/assets/41661cb8-9f45-4883-9a7b-63c531872c7f)

🚀 Cài đặt và chạy ứng dụng
1️⃣ Clone repository

bash
Sao chép
Chỉnh sửa
git clone https://github.com/Maple1302/maple_cinema.git
cd movie-app
2️⃣ Chạy trên Android Studio

Mở dự án bằng Android Studio
Chạy trên trình giả lập hoặc thiết bị thật
📂 Cấu trúc thư mục
📂 app/
 ├── 📂 data/          # Xử lý dữ liệu (API, Database)
 ├── 📂 ui/            # UI Components (Jetpack Compose)
 ├── 📂 di/            # Dependency Injection (Hilt)
 ├── 📂 model/         # Các model dữ liệu
 ├── 📂 repository/    # Repository Pattern
 ├── 📂 viewmodel/     # ViewModel cho MVVM
🎯 TODO (Cải thiện trong tương lai)
 Thêm tính năng đăng nhập
 Hỗ trợ Dark Mode
 Tối ưu performance
👨‍💻 Tác giả
📌 Maple – GitHub | LinkedIn

