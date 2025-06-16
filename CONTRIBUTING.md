# 🤝 Hướng dẫn đóng góp - ChiaSeTaiLieu

Chúng tôi rất hoan nghênh sự đóng góp từ cộng đồng! Tài liệu này hướng dẫn cách bạn có thể đóng góp vào dự án ChiaSeTaiLieu.

## 📋 Mục lục

- [Code of Conduct](#code-of-conduct)
- [Cách đóng góp](#cách-đóng-góp)
- [Báo cáo Bug](#báo-cáo-bug)
- [Đề xuất tính năng mới](#đề-xuất-tính-năng-mới)
- [Pull Request Process](#pull-request-process)
- [Coding Standards](#coding-standards)
- [Testing Guidelines](#testing-guidelines)

## 🤝 Code of Conduct

Dự án này tuân thủ [Contributor Covenant Code of Conduct](https://www.contributor-covenant.org/). Bằng cách tham gia, bạn cam kết tuân thủ các quy tắc này.

### Những hành vi được khuyến khích:
- Sử dụng ngôn ngữ chào đón và bao dung
- Tôn trọng quan điểm và kinh nghiệm khác nhau
- Nhận phê bình xây dựng một cách thanh lịch
- Tập trung vào điều tốt nhất cho cộng đồng

### Những hành vi không được chấp nhận:
- Sử dụng ngôn ngữ hoặc hình ảnh khiêu dâm
- Trolling, bình luận xúc phạm/chính trị
- Quấy rối công khai hoặc riêng tư
- Công bố thông tin riêng tư của người khác

## 🛠️ Cách đóng góp

### 1. Fork Repository
```bash
# Fork repository về GitHub account của bạn
# Sau đó clone về máy local
git clone https://github.com/chtr302/ChiaSeTaiLieu.git
cd ChiaSeTaiLieu
```

### 2. Thiết lập môi trường phát triển
```bash
# Setup thủ công
cd chiasetailieuhoctapptit
mvn clean install
```

### 3. Tạo branch mới
```bash
# Tạo branch cho feature/bugfix
git checkout -b feature/feature-name
# hoặc
git checkout -b bugfix/bug-description
```

### 4. Thực hiện thay đổi
- Viết code theo [Coding Standards](#coding-standards)
- Thêm tests cho code mới
- Đảm bảo tất cả tests pass
- Update documentation nếu cần

### 5. Commit thay đổi
```bash
git add .
git commit -m "feat: add new feature description"
```

### 6. Push và tạo Pull Request
```bash
git push origin feature/feature-name
# Sau đó tạo Pull Request trên GitHub
```

## 🐛 Báo cáo Bug

### Trước khi báo cáo:
1. Kiểm tra [Issues](https://github.com/chtr302/ChiaSeTaiLieu/issues) xem bug đã được báo cáo chưa
2. Đảm bảo bạn đang sử dụng phiên bản mới nhất
3. Kiểm tra [Troubleshooting](README.md#troubleshooting) trong README

### Cách báo cáo Bug:
Sử dụng [Bug Report Template](.github/ISSUE_TEMPLATE/bug_report.md) và cung cấp:

- **Mô tả ngắn gọn**: Tóm tắt bug trong 1-2 câu
- **Môi trường**: OS, Java version, Browser (nếu có)
- **Các bước tái hiện**: Liệt kê chi tiết cách tái hiện bug
- **Kết quả mong đợi**: Ứng dụng nên hoạt động như thế nào
- **Kết quả thực tế**: Điều gì đã xảy ra
- **Screenshots**: Nếu có thể, đính kèm ảnh chụp màn hình
- **Logs**: Attach relevant log files

### Ví dụ Bug Report:
```markdown
**Bug Description:**
Upload file PDF bị lỗi khi file lớn hơn 10MB

**Environment:**
- OS: Windows 10
- Java: 17.0.2
- Browser: Chrome 98.0

**Steps to Reproduce:**
1. Đăng nhập vào hệ thống
2. Vào trang Upload
3. Chọn file PDF có dung lượng 15MB
4. Click "Upload"

**Expected Result:**
File được upload thành công

**Actual Result:**
Hiển thị lỗi "Maximum upload size exceeded"

**Screenshots:**
[Attach screenshot]

**Additional Context:**
Lỗi này xảy ra với tất cả file lớn hơn 10MB
```

## 💡 Đề xuất tính năng mới

### Cách đề xuất:
1. Kiểm tra [Issues](https://github.com/chtr302/ChiaSeTaiLieu/issues) và [Roadmap](README.md#roadmap)
2. Sử dụng [Feature Request Template](.github/ISSUE_TEMPLATE/feature_request.md)
3. Mô tả chi tiết tính năng và lý do cần thiết

### Template Feature Request:
```markdown
**Tính năng mong muốn:**
Thêm chức năng notification real-time

**Vấn đề hiện tại:**
User không biết khi có comment mới hoặc vote cho tài liệu của mình

**Giải pháp đề xuất:**
- WebSocket notification system
- Email notification (optional)
- In-app notification bell icon

**Lợi ích:**
- Tăng engagement của user
- Cải thiện trải nghiệm người dùng
- Tăng tương tác trong cộng đồng

**Tài nguyên bổ sung:**
- Mockup designs
- Similar features in other platforms
```

## 🔄 Pull Request Process

### Checklist trước khi tạo PR:
- [ ] Code tuân thủ [Coding Standards](#coding-standards)
- [ ] Tất cả tests đều pass (`mvn test`)
- [ ] Code có đủ test coverage
- [ ] Documentation được cập nhật
- [ ] Commit messages theo format chuẩn
- [ ] Branch được rebase với main branch

### Quy trình Review:
1. **Automatic Checks**: CI/CD sẽ chạy tests và code quality checks
2. **Code Review**: Ít nhất 1 maintainer sẽ review code
3. **Testing**: Reviewer sẽ test tính năng manually
4. **Approval**: PR được approve và merge

### PR Title Format:
```
<type>(<scope>): <description>

Ví dụ:
feat(documents): add vote system for documents
fix(auth): resolve OAuth2 login issue
docs(readme): update installation guide
```

### PR Description Template:
```markdown
## Changes Made
- Brief description of changes
- List of modified files/features

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests pass
- [ ] Manual testing completed

## Screenshots (if applicable)
[Add screenshots for UI changes]

## Breaking Changes
- List any breaking changes
- Migration steps if needed

## Closes Issues
Closes #123, #456
```

## 📝 Coding Standards

### Java Code Style:
```java
// ✅ Good
@Service
public class DocumentService {
    
    @Autowired
    private DocumentRepository documentRepository;
    
    /**
     * Lấy tài liệu theo ID
     * @param id ID của tài liệu
     * @return TaiLieu object hoặc null
     */
    public TaiLieu getDocumentById(Long id) {
        return documentRepository.findById(id).orElse(null);
    }
}

// ❌ Bad
@Service
public class documentService{
    @Autowired private DocumentRepository repo;
    public TaiLieu getDoc(Long id){return repo.findById(id).orElse(null);}
}
```

### Naming Conventions:
- **Classes**: PascalCase (`DocumentService`, `TaiLieuController`)
- **Methods**: camelCase (`getDocumentById`, `saveTaiLieu`)
- **Variables**: camelCase (`documentId`, `taiLieuList`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_FILE_SIZE`, `DEFAULT_PAGE_SIZE`)
- **Database**: snake_case (`ma_tai_lieu`, `ngay_dang`)

### Code Organization:
```
controller/
├── DocumentController.java     # Document-related endpoints
├── UserController.java        # User management
└── AuthController.java        # Authentication

service/
├── document/
│   ├── DocumentService.java   # Core document logic
│   └── DocumentValidator.java # Validation logic
└── user/
    └── UserService.java       # User management logic

model/
├── entity/                    # JPA Entities
├── dto/                      # Data Transfer Objects
└── view/                     # Database Views
```

### Comments và Documentation:
```java
/**
 * Xử lý upload tài liệu từ người dùng
 * 
 * @param file File được upload
 * @param metadata Thông tin metadata của tài liệu
 * @param principal Thông tin người dùng đăng nhập
 * @return ResponseEntity với thông báo kết quả
 * @throws IOException Khi có lỗi xử lý file
 */
@PostMapping("/upload")
public ResponseEntity<?> uploadDocument(
        @RequestParam("file") MultipartFile file,
        @RequestParam("metadata") DocumentMetadata metadata,
        @AuthenticationPrincipal OidcUser principal) throws IOException {
    
    // Validate file size
    if (file.getSize() > MAX_FILE_SIZE) {
        throw new FileSizeExceededException("File quá lớn");
    }
    
    // Process upload
    Document document = documentService.processUpload(file, metadata, principal);
    
    return ResponseEntity.ok(document);
}
```

### Error Handling:
```java
// ✅ Good - Specific exception handling
try {
    documentService.saveDocument(document);
} catch (DocumentValidationException e) {
    return ResponseEntity.badRequest()
        .body(Map.of("error", "Validation failed", "details", e.getMessage()));
} catch (StorageException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", "Storage error", "details", "Cannot save file"));
}

// ❌ Bad - Generic exception handling
try {
    documentService.saveDocument(document);
} catch (Exception e) {
    return ResponseEntity.badRequest().body("Error: " + e.getMessage());
}
```

## 🧪 Testing Guidelines

### Unit Tests:
```java
@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
    
    @Mock
    private DocumentRepository documentRepository;
    
    @InjectMocks
    private DocumentService documentService;
    
    @Test
    @DisplayName("Should return document when valid ID is provided")
    void shouldReturnDocumentWhenValidId() {
        // Given
        Long documentId = 1L;
        TaiLieu expectedDocument = new TaiLieu();
        expectedDocument.setId(documentId);
        
        when(documentRepository.findById(documentId))
            .thenReturn(Optional.of(expectedDocument));
        
        // When
        TaiLieu result = documentService.getDocumentById(documentId);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(documentId);
        verify(documentRepository).findById(documentId);
    }
}
```

### Integration Tests:
```java
@SpringBootTest
@Transactional
class DocumentControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void shouldUploadDocumentSuccessfully() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file", "test.pdf", "application/pdf", "test content".getBytes());
        
        mockMvc.perform(multipart("/documents/upload")
                .file(file)
                .param("title", "Test Document")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
```

### Test Coverage:
- Mục tiêu: Ít nhất 80% line coverage
- Test tất cả business logic methods
- Test các edge cases và error scenarios
- Mock external dependencies

## 🏷️ Git Commit Guidelines

### Commit Message Format:
```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types:
- **feat**: Tính năng mới
- **fix**: Sửa bug
- **docs**: Thay đổi documentation
- **style**: Code formatting (không ảnh hưởng logic)
- **refactor**: Refactor code
- **test**: Thêm hoặc sửa tests
- **chore**: Maintenance tasks

### Examples:
```bash
feat(documents): add vote system for documents

- Add upvote/downvote functionality
- Update document detail page UI
- Add vote count display

Closes #123

fix(auth): resolve OAuth2 redirect issue

The OAuth2 redirect was failing due to incorrect URI configuration.
Updated the redirect URI in application properties.

Fixes #456

docs(readme): update installation guide

- Add detailed setup instructions
- Include troubleshooting section
- Update dependency versions
```

## 🚀 Release Process

### Version Numbering:
- **Major**: Breaking changes (1.0.0 → 2.0.0)
- **Minor**: New features, backward compatible (1.0.0 → 1.1.0)
- **Patch**: Bug fixes (1.0.0 → 1.0.1)

### Release Checklist:
- [ ] All tests pass
- [ ] Documentation updated
- [ ] CHANGELOG.md updated
- [ ] Version bumped in pom.xml
- [ ] Release notes prepared
- [ ] Security review completed

## 📞 Hỗ trợ

Nếu bạn cần hỗ trợ:

- 💬 **Discussion**: [GitHub Discussions](https://github.com/chtr302/ChiaSeTaiLieu/discussions)
- 🐛 **Issues**: [GitHub Issues](https://github.com/chtr302/ChiaSeTaiLieu/issues)
- 📧 **Email**: dev@chiasetailieu.com
- 📖 **Documentation**: [Project Wiki](https://github.com/chtr302/ChiaSeTaiLieu/wiki)

## 🙏 Ghi nhận đóng góp

### 🏆 Core Contributors:
- **Vũ Phạm Minh Thức** *(Nhóm trưởng)* - Thiết kế UI/UX, phát triển frontend, xây dựng chức năng lưu/tải về/bình luận/báo cáo
- **Trần Công Hậu** - Kiến trúc dự án, thiết kế hệ thống, phát triển tính năng AI và xác thực Google OAuth2
- **Nguyễn Minh Quân** - Thiết kế cơ sở dữ liệu, phát triển chức năng upload, tìm kiếm và quản lý thư viện

### 📝 Contribution Recognition:
Tất cả các đóng góp sẽ được ghi nhận trong:
- README.md Contributors section
- Release notes and changelogs
- GitHub contributors page
- Special recognition for significant contributions

Cảm ơn bạn đã đóng góp vào ChiaSeTaiLieu! 🎉 