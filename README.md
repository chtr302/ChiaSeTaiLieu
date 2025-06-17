# 📚 ChiaSeTaiLieu - Document Sharing Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 🎯 Giới thiệu

**ChiaSeTaiLieu** là một nền tảng chia sẻ tài liệu học tập dành cho sinh viên, được phát triển bởi đội ngũ PTIT. Ứng dụng cho phép sinh viên upload, chia sẻ, đánh giá và thảo luận về các tài liệu học tập một cách dễ dàng và hiệu quả.

### ✨ Tính năng chính

- 🔐 **Xác thực OAuth2** - Đăng nhập bằng Google Account
- 📤 **Upload tài liệu** - Hỗ trợ nhiều định dạng file (PDF, DOC, PPT, ...)
- 🔍 **Tìm kiếm thông minh** - Tìm kiếm có dấu/không dấu tiếng Việt
- 👍 **Hệ thống đánh giá** - Upvote/Downvote tài liệu
- 💬 **Bình luận & Thảo luận** - Comment và reply cho tài liệu
- 🚩 **Báo cáo vi phạm** - Report tài liệu hoặc bình luận không phù hợp
- 📊 **Thống kê chi tiết** - Lượt xem, lượt tải, đánh giá
- 🤖 **Tích hợp AI** - Chatbot thông minh, tóm tắt tài liệu và Q&A tự động
- 📱 **Giao diện responsive** - Tương thích mobile và desktop
- 🗂️ **Phân loại theo môn học** - Tổ chức tài liệu theo chuyên ngành

## 🛠️ Công nghệ sử dụng

### Backend
- **Spring Boot 3.4.6** - Framework chính
- **Spring Security** - Bảo mật và xác thực
- **Spring Data JPA** - ORM và database access
- **Spring WebFlux** - Reactive web framework
- **MySQL 8.0** - Cơ sở dữ liệu chính
- **Apache PDFBox** - Xử lý file PDF
- **OAuth2 Client** - Xác thực Google

### Frontend
- **Thymeleaf** - Template engine
- **HTML5/CSS3** - Giao diện người dùng
- **JavaScript** - Tương tác client-side
- **Bootstrap/FontAwesome** - UI components

### AI & Machine Learning
- **Gemma AI Model** - Large Language Model để tóm tắt và Q&A
- **AI Chatbot** - Hỗ trợ tìm kiếm và trả lời câu hỏi
- **Document Summarization** - Tóm tắt tài liệu tự động
- **Q&A System** - Hỏi đáp về nội dung tài liệu

### Tools & Utilities
- **Maven** - Dependency management
- **CSRF Protection** - Bảo mật web
- **File Upload** - Quản lý file đa phương tiện

## 📚 **Tài liệu bổ sung**

- 🚀 **[Quick Start Guide](QUICK_START.md)** - Hướng dẫn chạy nhanh trong 5 phút
- 🤝 **[Contributing Guidelines](CONTRIBUTING.md)** - Hướng dẫn đóng góp cho dự án  
- 📝 **[Changelog](CHANGELOG.md)** - Lịch sử phiên bản và thay đổi

## 📋 Yêu cầu hệ thống

### Phần mềm bắt buộc
- **Java 17+** 
- **MySQL 8.0+**
- **Maven 3.6+**
- **Git**

### Tùy chọn (Đã triển khai)
- **AI Service** - External AI API (port 8000) - Đã tích hợp với Gemma AI
- **Google OAuth2** - Credentials for authentication

## 🚀 Cài đặt và Triển khai

### 1. Clone Repository

```bash
git clone https://github.com/chtr302/ChiaSeTaiLieu.git
cd ChiaSeTaiLieu/chiasetailieuhoctapptit
```

### 2. Cấu hình Database

#### Tạo database MySQL:
```sql
CREATE DATABASE chiasetailieu;
CREATE USER 'chiasetailieu_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON chiasetailieu.* TO 'chiasetailieu_user'@'localhost';
FLUSH PRIVILEGES;
```

#### Import database schema:
```bash
mysql -u chiasetailieu_user -p chiasetailieu < database/schema.sql
```

### 3. Cấu hình ứng dụng

#### a) Tạo file cấu hình secret

Copy file mẫu và điền thông tin thực tế:

```bash
cd src/main/resources/
cp application-secret.properties.example application-secret.properties
```

#### b) Cấu hình Google OAuth2

1. Truy cập [Google Cloud Console](https://console.cloud.google.com/)
2. Tạo project mới hoặc chọn project hiện có
3. Bật Google+ API và Google OAuth2 API
4. Tạo OAuth 2.0 Client ID:
   - Application type: Web application
   - Authorized redirect URIs: `http://localhost:8080/login/oauth2/code/google`
5. Copy Client ID và Client Secret vào file `application-secret.properties`

#### c) Chỉnh sửa file `application-secret.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/chiasetailieu?useSSL=false&serverTimezone=UTC
spring.datasource.username=chiasetailieu_user
spring.datasource.password=YOUR_ACTUAL_DB_PASSWORD

# OAuth2 Google Configuration - THAY ĐỔI BẰNG THÔNG TIN THỰC TẾ
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=openid,profile,email
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google

# File Upload Configuration
upload.dir=./uploads
thumbnail.dir=./thumbnails

# AI Service Configuration - Tích hợp Gemma AI
ai.service.serviceUrl=http://localhost:8000
ai.service.enabled=true
```

⚠️ **LƯU Ý QUAN TRỌNG**: 
- File `application-secret.properties` chứa thông tin nhạy cảm
- KHÔNG commit file này lên GitHub
- File này đã được thêm vào `.gitignore`

### 4. Tạo thư mục lưu trữ

```bash
mkdir uploads
mkdir thumbnails
chmod 755 uploads thumbnails
```

### 4. Build và Run

```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

Hoặc:

```bash
# Build JAR file
mvn clean package

# Run JAR
java -jar target/chiasetailieuhoctapptit-0.0.1-SNAPSHOT.jar
```

### 5. Truy cập ứng dụng

Mở trình duyệt và truy cập: `http://localhost:8080`

## 📖 Hướng dẫn sử dụng

### 🔑 Đăng nhập
1. Truy cập trang chủ
2. Click "Đăng nhập" 
3. Chọn "Login with Google"
4. Xác thực bằng Google Account

### 📤 Upload tài liệu
1. Đăng nhập vào hệ thống
2. Vào trang "Upload" 
3. Chọn file tài liệu
4. Điền thông tin: Tiêu đề, Mô tả, Môn học, Loại tài liệu
5. Thêm tags (tùy chọn)
6. Click "Upload"

### 🔍 Tìm kiếm tài liệu
1. **Tìm kiếm thường:** Sử dụng thanh search trên navbar
2. **Tìm kiếm AI:** Sử dụng chatbot AI để tìm tài liệu thông minh
3. Nhập từ khóa (hỗ trợ tiếng Việt có dấu)
4. Hoặc browse theo:
   - **Môn học** - Xem tài liệu theo từng môn
   - **Loại tài liệu** - Bài giảng, đề thi, bài tập...
   - **Most viewed** - Tài liệu được xem nhiều nhất

### 👍 Đánh giá tài liệu
1. Vào trang chi tiết tài liệu
2. Click nút **👍 Upvote** nếu hữu ích
3. Click nút **👎 Downvote** nếu không phù hợp
4. Click lại để bỏ đánh giá

### 🤖 Sử dụng AI Features
1. **Tóm tắt tài liệu tự động:** Khi upload tài liệu, AI sẽ tự động tạo tóm tắt
2. **Chatbot tìm kiếm:** Click vào icon chatbot để tìm kiếm tài liệu bằng AI
3. **Q&A tài liệu:** Trong trang chi tiết tài liệu, click "Hỏi AI" để đặt câu hỏi về nội dung
4. **Chat sessions:** AI sẽ nhớ ngữ cảnh cuộc trò chuyện để trả lời chính xác hơn

### 💬 Bình luận và thảo luận
1. Trong trang chi tiết, scroll xuống phần Comment
2. Nhập nội dung bình luận
3. Click "Post" để gửi
4. **Reply**: Click "Reply" trên bình luận để trả lời
5. **Report**: Click "Report" để báo cáo bình luận vi phạm

### 🚩 Báo cáo vi phạm
1. **Báo cáo tài liệu**: Click nút "Report" trong sidebar
2. **Báo cáo bình luận**: Click "Report" trên bình luận
3. Chọn lý do và mô tả chi tiết
4. Submit báo cáo

### 💾 Lưu tài liệu
1. Click icon "Save" trên tài liệu
2. Xem tài liệu đã lưu trong trang "Library"

## 🏗️ Cấu trúc dự án

```
chiasetailieuhoctapptit/
├── src/
│   ├── main/
│   │   ├── java/com/chiasetailieu/chiasetailieuhoctapptit/
│   │   │   ├── controller/          # REST Controllers
│   │   │   │   ├── DocumentController.java
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── UploadController.java
│   │   │   │   ├── LibraryController.java
│   │   │   │   └── VoteController.java
│   │   │   ├── service/             # Business Logic
│   │   │   │   ├── TaiLieu/
│   │   │   │   ├── BinhLuan/
│   │   │   │   ├── VoteService/
│   │   │   │   └── SinhVien/
│   │   │   ├── model/               # JPA Entities
│   │   │   │   ├── TaiLieuModel/
│   │   │   │   ├── BinhLuanModel/
│   │   │   │   ├── VoteModel/
│   │   │   │   └── SinhVienModel/
│   │   │   ├── repository/          # Data Access Layer
│   │   │   └── config/              # Configuration
│   │   └── resources/
│   │       ├── templates/           # Thymeleaf Templates
│   │       │   ├── documents.html
│   │       │   ├── documents-detail.html
│   │       │   ├── upload.html
│   │       │   └── library.html
│   │       ├── static/              # Static Resources
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── img/
│   │       └── application.properties
├── uploads/                         # Uploaded Files
├── thumbnails/                      # File Thumbnails
└── pom.xml                         # Maven Dependencies
```

## 🔌 API Endpoints

### Authentication
- `GET /signin` - Trang đăng nhập
- `GET /oauth2/authorization/google` - OAuth2 Google login

### Documents
- `GET /documents` - Danh sách tài liệu
- `GET /documents/detail/{id}` - Chi tiết tài liệu  
- `POST /documents/upload` - Upload tài liệu
- `GET /documents/download/{id}` - Tải xuống tài liệu
- `GET /documents/view/{id}` - Xem tài liệu online

### Vote System
- `POST /api/vote/upvote` - Upvote tài liệu
- `POST /api/vote/downvote` - Downvote tài liệu
- `GET /api/vote/info/{id}` - Thông tin vote

### Comments
- `POST /documents/comment/{id}` - Thêm bình luận
- `POST /documents/reply/{commentId}` - Trả lời bình luận

### Reports
- `POST /documents/report/{documentId}` - Báo cáo tài liệu
- `POST /documents/report-comment/{commentId}` - Báo cáo bình luận

### AI Integration
- `GET /ai/health` - Kiểm tra trạng thái AI service
- `POST /ai/summarize-upload` - Tóm tắt tài liệu upload
- `POST /ai/create-session` - Tạo Q&A session cho tài liệu
- `POST /ai/session/ask` - Đặt câu hỏi trong session
- `GET /ai/session/history` - Lấy lịch sử chat session
- `POST /ai/ask-question` - Hỏi trực tiếp về tài liệu
- `POST /ai/chatbot/session` - Tạo chatbot session
- `POST /ai/chatbot/chat` - Chat với AI assistant
- `POST /ai/chatbot/auto` - Auto chat với context

### Library
- `POST /documents/save/{id}` - Lưu/Bỏ lưu tài liệu
- `GET /library` - Thư viện cá nhân

## 🔧 Configuration

### File Upload Limits
```properties
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

### Database Connection Pool
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

### Security Settings
```properties
spring.security.oauth2.client.registration.google.client-name=Google
```

## 🐛 Troubleshooting

### Lỗi thường gặp

#### 1. **Database Connection Error**
```
Caused by: java.sql.SQLException: Access denied for user
```
**Giải pháp**: Kiểm tra username/password trong `application-secret.properties`

#### 2. **OAuth2 Login Failed**
```
OAuth2 authorization failed
```
**Giải pháp**: 
- Kiểm tra Google Client ID/Secret
- Đảm bảo redirect URI đúng trong Google Console

#### 3. **File Upload Error**
```
Maximum upload size exceeded
```
**Giải pháp**: Tăng `max-file-size` trong properties

#### 4. **Port Already in Use**
```
Port 8080 was already in use
```
**Giải pháp**: 
```bash
# Kill process sử dụng port 8080
lsof -ti:8080 | xargs kill -9

# Hoặc đổi port
server.port=8081
```

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests  
```bash
mvn verify
```

### Test Coverage
```bash
mvn jacoco:report
```

## 📊 Database Schema

### Key Tables
- **SinhVien** - Thông tin sinh viên
- **TaiLieu** - Thông tin tài liệu
- **BinhLuan** - Bình luận
- **Vote** - Đánh giá tài liệu
- **File** - Metadata file upload
- **BaoCao** - Báo cáo vi phạm

### Views
- **vw_TaiLieu** - View tổng hợp thông tin tài liệu
- **vw_BinhLuan** - View bình luận với thông tin user

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

### Coding Standards
- Sử dụng Java 17 features
- Follow Spring Boot best practices
- Comment code bằng tiếng Việt
- Write unit tests cho các features mới

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.

## 👥 Contributors

- **Vũ Phạm Minh Thức** *(Nhóm trưởng)* - Thiết kế giao diện thư viện cá nhân và trang tài liệu, xây dựng chức năng lưu, tải về, bình luận và báo cáo tài liệu - [GitHub](https://github.com/Wake0801)
- **Trần Công Hậu** - Xây dựng chức năng đăng nhập Google, thiết kế cấu trúc dự án, phát triển các chức năng AI (tóm tắt tài liệu, chatbot) - [GitHub](https://github.com/chtr302)  
- **Nguyễn Minh Quân** - Thiết kế cơ sở dữ liệu và giao diện, xây dựng chức năng upload, chỉnh sửa thông tin và tìm kiếm tài liệu - [GitHub](https://github.com/minhquaan04)

## 📞 Support

Nếu gặp vấn đề hoặc có câu hỏi:

- 📧 Email: support@chiasetailieu.com
- 🐛 Issues: [GitHub Issues](https://github.com/chtr302/ChiaSeTaiLieu/issues)
- 📖 Wiki: [Project Wiki](https://github.com/chtr302/ChiaSeTaiLieu/wiki)

## 🎯 Roadmap

### Version 2.0
- [ ] Real-time notifications
- [ ] Advanced search filters
- [ ] Document versioning
- [ ] Admin dashboard
- [ ] API rate limiting
- [ ] Email notifications
- [ ] Dark mode theme

### Version 3.0
- [ ] Mobile app (Android/iOS)
- [x] Advanced AI features - Đã hoàn thành: Chatbot, Q&A, Document Summarization
- [ ] Document collaboration
- [ ] Integration with LMS systems

---

⭐ **Star this repo if you find it helpful!**

Made with ❤️ by PTIT Team - chtr302, minhquaan04, Wake0801 