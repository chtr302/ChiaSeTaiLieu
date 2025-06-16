# 🚀 Quick Start Guide - ChiaSeTaiLieu

Hướng dẫn nhanh để chạy dự án trong vòng 5 phút!

## ⚡ TL;DR - Chạy ngay

```bash
# 1. Clone repository
git clone https://github.com/chtr302/ChiaSeTaiLieu.git
cd ChiaSeTaiLieu

# 2. Chạy script setup tự động
chmod +x setup.sh
./setup.sh

# 3. Cấu hình Google OAuth2 (xem bên dưới)

# 4. Chạy ứng dụng
cd chiasetailieuhoctapptit
mvn spring-boot:run
```

## 🔑 Cấu hình Google OAuth2 (BẮT BUỘC)

### Bước 1: Tạo Google Cloud Project
1. Vào [Google Cloud Console](https://console.cloud.google.com/)
2. Tạo project mới hoặc chọn project có sẵn
3. Bật **Google+ API** và **Google Identity Services**

### Bước 2: Tạo OAuth2 Credentials
1. Vào **Credentials** → **Create Credentials** → **OAuth 2.0 Client IDs**
2. Chọn **Web Application**
3. Thêm **Authorized redirect URIs**: 
   ```
   http://localhost:8080/login/oauth2/code/google
   ```
4. Copy **Client ID** và **Client Secret**

### Bước 3: Cập nhật cấu hình
Chỉnh sửa file `chiasetailieuhoctapptit/src/main/resources/application-secret.properties`:

```properties
# Thay YOUR_GOOGLE_CLIENT_ID và YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
```

## 🗄️ Cấu hình Database (Nếu cần)

### Option 1: MySQL (Recommended)
```sql
CREATE DATABASE chiasetailieu;
CREATE USER 'chiasetailieu_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON chiasetailieu.* TO 'chiasetailieu_user'@'localhost';
```

### Option 2: H2 Database (Testing only)
Thêm vào `application-secret.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

## 📁 Cấu trúc quan trọng

```
ChiaSeTaiLieu/
├── README.md                              # 📖 Tài liệu chính
├── setup.sh                               # 🔧 Script setup tự động
├── chiasetailieuhoctapptit/               # 💻 Mã nguồn chính
│   ├── src/main/resources/
│   │   ├── application.properties         # ⚙️ Config chung
│   │   ├── application-secret.properties  # 🔒 Config nhạy cảm (KHÔNG commit)
│   │   └── application-secret.properties.example # 📋 Template config
│   └── pom.xml                           # 📦 Maven dependencies
├── uploads/                               # 📂 Thư mục lưu file upload
└── thumbnails/                           # 🖼️ Thư mục thumbnail
```

## 🌐 Truy cập ứng dụng

Sau khi chạy thành công:
- **URL**: http://localhost:8080
- **Login**: Sử dụng Google Account

## 🐛 Troubleshooting nhanh

| Lỗi | Nguyên nhân | Giải pháp |
|-----|-------------|-----------|
| `OAuth2 failed` | Sai Google credentials | Kiểm tra Client ID/Secret |
| `Database error` | MySQL chưa chạy | `sudo service mysql start` |
| `Port 8080 in use` | Port bị chiếm | `lsof -ti:8080 \| xargs kill -9` |
| `File upload error` | Thiếu thư mục | `mkdir uploads thumbnails` |

## 📞 Cần hỗ trợ?

- 📖 **Chi tiết**: [README.md](README.md)
- 🐛 **Báo lỗi**: [GitHub Issues](https://github.com/chtr302/ChiaSeTaiLieu/issues)
- 💬 **Thảo luận**: [GitHub Discussions](https://github.com/chtr302/ChiaSeTaiLieu/discussions)

## 🎯 Next Steps

1. ✅ Đăng nhập bằng Google
2. ✅ Upload tài liệu đầu tiên
3. ✅ Test vote, comment, report
4. ✅ Khám phá các tính năng khác

**Happy Coding! 🎉** 