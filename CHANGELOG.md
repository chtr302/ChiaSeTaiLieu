# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### 🚀 Planned Features
- [ ] Real-time notifications
- [ ] Advanced search filters
- [ ] Document versioning
- [ ] Admin dashboard
- [ ] API rate limiting
- [ ] Email notifications
- [ ] Dark mode theme

## [1.1.0] - 2024-01-25 🤖 AI Integration Release

### ✨ Added - AI & Machine Learning Features
- **Gemma AI Integration**
  - Complete AI service architecture with WebClient
  - External AI API integration (port 8000)
  - Reactive HTTP communication for AI services

- **Document Summarization**
  - Automatic AI-generated summaries for uploaded documents
  - Asynchronous processing with polling mechanism
  - Support for multiple document formats
  - Summary result caching and retrieval

- **AI-Powered Q&A System**
  - Interactive document question-answering
  - Context-aware responses based on document content
  - Session-based conversation management
  - Chat history tracking and retrieval

- **Smart Chatbot Assistant**
  - General platform guidance and help
  - Document search assistance with AI recommendations
  - Context-aware conversations with memory
  - Multiple chat types (general, search, document-specific)

- **AI Search Enhancement**
  - Intelligent document discovery
  - Content-based recommendations
  - Semantic search capabilities
  - User query understanding and processing

### 🔧 Technical Implementation
- **New Controllers & Services**
  - `AIController` - AI API endpoints management
  - `AIService` - Business logic for AI operations
  - WebClient configuration for external AI service

- **API Endpoints Added**
  - `GET /ai/health` - AI service health check
  - `POST /ai/summarize-upload` - Document summarization
  - `POST /ai/create-session` - Q&A session creation
  - `POST /ai/session/ask` - Question in session
  - `GET /ai/session/history` - Chat history
  - `POST /ai/ask-question` - Direct document questioning
  - `POST /ai/chatbot/session` - Chatbot session management
  - `POST /ai/chatbot/chat` - AI assistant communication
  - `POST /ai/chatbot/auto` - Auto chat with context

- **Frontend Integration**
  - AI chatbot UI components with Gemma branding
  - Document Q&A interface in detail pages
  - Real-time chat interface with typing indicators
  - Session management for continuous conversations

### 🔧 Configuration Updates
- AI service URL configuration
- External service timeout and retry logic
- Error handling for AI service unavailability
- Performance optimization for AI calls

### 👨‍💻 Developer Credits
- **Trần Công Hậu** - Complete AI system architecture and implementation, Gemma AI integration, chatbot development, document summarization, Q&A system, and AI-powered search functionality

## [1.0.0] - 2024-01-15

### ✨ Added
- **Authentication System**
  - OAuth2 Google authentication
  - User session management
  - CSRF protection
  - Secure logout functionality

- **Document Management**
  - Upload multiple file formats (PDF, DOC, PPT, etc.)
  - File validation and size limits (50MB)
  - Automatic thumbnail generation
  - Document categorization by subject and type
  - Tag system for better organization

- **Vote System**
  - Upvote/Downvote functionality
  - Smart vote counting with display logic
  - Vote status tracking per user
  - Real-time vote updates

- **Comment System**
  - Add comments to documents
  - Reply to comments (nested structure)
  - Comment author information display
  - Date/time stamps for all comments

- **Report System**
  - Report inappropriate documents
  - Report inappropriate comments
  - Admin review workflow
  - Detailed report tracking

- **Search & Discovery**
  - Vietnamese text search (with/without accents)
  - Filter by course/subject
  - Filter by document type
  - Most viewed documents
  - Search suggestions API

- **User Library**
  - Save/bookmark documents
  - Personal document library
  - Following courses feature
  - User profile with statistics

- **File Handling**
  - Secure file upload with validation
  - PDF viewer integration
  - File download with proper headers
  - Thumbnail generation and caching

- **UI/UX Features**
  - Responsive design (mobile/desktop)
  - Modern, clean interface
  - Loading states and error handling
  - Success/error notifications
  - Pagination for large datasets

### 🔧 Technical Features
- **Backend Architecture**
  - Spring Boot 3.4.6 framework
  - MySQL 8.0 database
  - JPA/Hibernate ORM
  - RESTful API design
  - Proper error handling and validation

- **Security**
  - OAuth2 authentication
  - CSRF protection
  - SQL injection prevention
  - File upload security
  - Session management

- **Performance**
  - Database connection pooling
  - Efficient SQL queries with views
  - File caching for thumbnails
  - Optimized frontend assets

- **Integration**
  - External AI service integration
  - PDF processing with Apache PDFBox
  - Reactive web with WebFlux

### 📊 Database Schema
- **Core Tables**
  - `SinhVien` - User information
  - `TaiLieu` - Document metadata
  - `BinhLuan` - Comments
  - `TraLoiBinhLuan` - Comment replies
  - `Vote` - Document votes
  - `File` - File metadata
  - `BaoCao` - Report system
  - `MonHoc` - Courses/Subjects
  - `LoaiTaiLieu` - Document types
  - `LuuTaiLieu` - Saved documents

- **Views**
  - `vw_TaiLieu` - Comprehensive document view
  - `vw_BinhLuan` - Comment view with user info

### 🛠️ Development Tools
- Maven build system
- Automated setup script
- Configuration examples
- Comprehensive documentation
- Testing framework setup

### 📝 Documentation
- Complete README with installation guide
- API documentation
- Contributing guidelines
- Code style standards
- Troubleshooting guide

## [0.9.0] - 2023-12-01

### ✨ Added
- Initial project setup
- Basic Spring Boot configuration
- Database schema design
- OAuth2 authentication prototype

### 🔧 Changed
- Project structure organization
- Dependency management setup

## [0.1.0] - 2023-11-15

### ✨ Added
- Project initialization
- Basic Maven configuration
- Initial development setup

---

## Legend

- ✨ **Added** - New features
- 🔧 **Changed** - Changes in existing functionality
- 🐛 **Fixed** - Bug fixes
- 🗑️ **Removed** - Removed features
- 🔒 **Security** - Security improvements
- ⚡ **Performance** - Performance improvements
- 📝 **Documentation** - Documentation updates

## Contributing

See our [Contributing Guidelines](CONTRIBUTING.md) for information on how to contribute to this changelog.

## Release Notes

For detailed release notes and migration guides, visit our [GitHub Releases](https://github.com/chtr302/ChiaSeTaiLieu/releases) page. 