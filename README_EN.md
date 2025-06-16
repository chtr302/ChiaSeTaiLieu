# ChiaSeTaiLieu - Document Sharing Platform

A document sharing platform specifically designed for PTIT students, enabling secure upload, sharing, and management of academic documents with advanced features like AI-powered document summarization and Q&A.

## 🚀 Features

### 📤 Document Management
- **Secure Upload**: Upload PDF documents up to 50MB with automatic validation
- **Smart Organization**: Categorize documents by subject, semester, and type
- **Advanced Search**: Find documents using Vietnamese text search with smart filters
- **Personal Library**: Manage your uploaded and saved documents

### 🔐 Authentication & Security
- **Google OAuth2**: Secure login using PTIT Google accounts
- **CSRF Protection**: Enhanced security for all user actions
- **Access Control**: Proper authorization for document operations

### 💬 Community Features
- **Voting System**: Upvote/downvote documents with smart display logic
- **Comments & Replies**: Engage in discussions about documents
- **Report System**: Flag inappropriate content or documents
- **User Profiles**: Track contributions and activity

### 🤖 AI-Powered Features
- **Document Summarization**: AI-generated summaries for quick understanding
- **Document Q&A**: Ask questions about document content
- **Smart Chatbot**: Get help and information about the platform

## 🛠️ Technical Stack

### Backend
- **Spring Boot 3.4.6** - Main framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Database operations
- **MySQL 8.0** - Primary database
- **Apache PDFBox** - PDF processing

### Frontend
- **Thymeleaf** - Server-side templating
- **Bootstrap 5** - Responsive UI framework
- **JavaScript** - Interactive features

### Build & Deployment
- **Maven** - Dependency management
- **GitHub** - Version control

## ⚡ Quick Start

### Prerequisites
- Java 21 or higher
- MySQL 8.0
- Maven 3.6+
- Google Cloud Console account (for OAuth2)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/chtr302/ChiaSeTaiLieu.git
   cd ChiaSeTaiLieu
   ```

2. **Configure database**
   ```sql
   CREATE DATABASE chiase_tailieu;
   CREATE USER 'app_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON chiase_tailieu.* TO 'app_user'@'localhost';
   ```

3. **Configure Google OAuth2**
   - Create project in Google Cloud Console
   - Enable Google+ API
   - Create OAuth2 credentials
   - Add redirect URI: `http://localhost:8080/login/oauth2/code/google`

4. **Set up configuration**
   ```bash
   cp src/main/resources/application-secret.properties.example src/main/resources/application-secret.properties
   ```
   Edit the file with your actual credentials.

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

6. **Access the application**
   Open http://localhost:8080 in your browser

## 📚 Usage Guide

### For Students
1. **Login** with your PTIT Google account
2. **Browse** available documents by category
3. **Search** for specific documents or topics
4. **Download** documents you need
5. **Vote** and **comment** on documents
6. **Upload** your own documents to share

### For Contributors
1. **Upload** high-quality documents
2. **Provide** accurate descriptions and tags
3. **Engage** with community through comments
4. **Help** moderate content through reporting

## 🔧 Configuration

### Environment Variables
Create `.env` file:
```bash
DB_HOST=localhost
DB_PORT=3306
DB_NAME=chiase_tailieu
DB_USER=app_user
DB_PASSWORD=your_password
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
```

### Application Properties
Key configurations in `application-secret.properties`:
- Database connection settings
- Google OAuth2 credentials
- File upload settings
- AI service configurations

## 🚦 API Endpoints

### Authentication
- `GET /login` - Login page
- `GET /oauth2/authorization/google` - Google OAuth2 login

### Documents
- `GET /` - Home page with document list
- `GET /document/{id}` - Document details
- `POST /upload` - Upload new document
- `GET /download/{id}` - Download document

### User Actions
- `POST /vote` - Vote on document
- `POST /comment` - Add comment
- `POST /report` - Report content

## 👥 Contributors

- **Vũ Phạm Minh Thức** *(Team Leader)* - Personal library and document page UI design, save/download/comment/report functionality - [GitHub](https://github.com/Wake0801)
- **Trần Công Hậu** - Google login implementation, project architecture, AI features (document summarization, chatbot) - [GitHub](https://github.com/chtr302)
- **Nguyễn Minh Quân** - Database and UI design, upload functionality, document editing and search features - [GitHub](https://github.com/minhquaan04)

## 📄 Documentation

- [Quick Start Guide](QUICK_START.md) - Get up and running quickly
- [Contributing Guidelines](CONTRIBUTING.md) - How to contribute to the project
- [Changelog](CHANGELOG.md) - Version history and updates

## 🤝 Contributing

We welcome contributions! Please read our [Contributing Guidelines](CONTRIBUTING.md) for details on:
- Code standards and style
- Pull request process
- Issue reporting
- Development workflow

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

- **Email**: support@chiasetailieu.com
- **GitHub Issues**: [Create an issue](https://github.com/chtr302/ChiaSeTaiLieu/issues)
- **Documentation**: [Project Wiki](https://github.com/chtr302/ChiaSeTaiLieu/wiki)

## 🎯 Project Status

**Current Version**: 1.0.0  
**Status**: Active Development  
**Last Updated**: January 2024

---

**ChiaSeTaiLieu** - Making academic resource sharing easier for PTIT students 🎓 